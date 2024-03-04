package com.travel.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.travel.constant.EntityTypeConstant;
import com.travel.constant.MessageConstant;
import com.travel.context.BaseContext;
import com.travel.dto.MessageConversationPageQueryDTO;
import com.travel.dto.MessageDTO;
import com.travel.dto.MessagePageQueryDTO;
import com.travel.dto.MessageUnreadDTO;
import com.travel.entity.Message;
import com.travel.entity.User;
import com.travel.exception.AccountNotFoundException;
import com.travel.mapper.MessageMapper;
import com.travel.mapper.UserMapper;
import com.travel.result.PageResult;
import com.travel.service.MessageService;
import com.travel.service.UserService;
import com.travel.vo.MessageVO;
import com.travel.vo.NotificationVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserService userService;

    /**
     * Save a new message
     * @param messageDTO
     * @return
     */
    public void save(MessageDTO messageDTO) {
        Message message = new Message();
        BeanUtils.copyProperties(messageDTO, message);

        // Get toId using toName
        User toUser = userMapper.getByUsername(messageDTO.getToName());
        if (toUser == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        Long toId = toUser.getId();
        message.setToId(toId);
        if (messageDTO.getFromId() < toId) {
            message.setConversationId(message.getFromId() + "_" + toId);
        } else {
            message.setConversationId(toId + "_" + message.getFromId());
        }

        message.setCreateTime(LocalDateTime.now());
        messageMapper.insert(message);
    }

    /**
     * Set status to read
     * @param conversationId
     */
    public void setRead(String conversationId) {

    }

    /**
     * Message Page query
     * @param messagePageQueryDTO
     * @return
     */
    public PageResult pageQuery(MessagePageQueryDTO messagePageQueryDTO) {
        PageHelper.startPage(messagePageQueryDTO.getPage(), messagePageQueryDTO.getPageSize());
        Page<Message> page = messageMapper.pageQuery(messagePageQueryDTO);
        Long userId = messagePageQueryDTO.getUserId();

        List<Message> messageList = page.getResult();
        List<MessageVO> messageVOList = new ArrayList<>();
        for (Message message : messageList) {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            MessageUnreadDTO messageUnreadDTO = new MessageUnreadDTO(userId, message.getConversationId());
            int unread = messageMapper.getUnreadCount(messageUnreadDTO);
            messageVO.setUnread(unread);
            // Get avatar
            Long targetId = userId == message.getFromId() ? message.getToId() : message.getFromId();
            User user = userService.findUserById(targetId);
            messageVO.setAvatar(user.getAvatar());
            messageVO.setUsername(user.getUsername());
            messageVOList.add(messageVO);
        }

        return new PageResult(page.getTotal(), messageVOList);
    }

    /**
     * Conversation Page query
     * @param messageConversationPageQueryDTO
     * @return
     */
    public PageResult conversationPageQuery(MessageConversationPageQueryDTO messageConversationPageQueryDTO) {
        PageHelper.startPage(messageConversationPageQueryDTO.getPage(), messageConversationPageQueryDTO.getPageSize());
        Page<Message> page = messageMapper.conversationPageQuery(messageConversationPageQueryDTO);

        List<Message> messageList = page.getResult();
        List<MessageVO> messageVOList = new ArrayList<>();
        for (Message message : messageList) {
            MessageVO messageVO = new MessageVO();
            BeanUtils.copyProperties(message, messageVO);
            // Get avatar
            User user = userService.findUserById(message.getFromId());
            messageVO.setAvatar(user.getAvatar());
            messageVO.setUsername(user.getUsername());
            messageVOList.add(messageVO);
        }

        // Set unread messages to read status
        List<Long> ids = getUnreadIds(messageList);
        if (!ids.isEmpty()) {
            messageMapper.update(ids, Message.READ_STATUS);
        }

        return new PageResult(page.getTotal(), messageVOList);
    }

    /**
     * Get unread ids
     * @param messageList
     * @return
     */
    private List<Long> getUnreadIds(List<Message> messageList) {
        List<Long> ids = new ArrayList<>();

        if (messageList != null) {
            for (Message message : messageList) {
                if (BaseContext.getCurrentId() == message.getToId() && message.getStatus() == 0) {
                    ids.add(message.getId());
                }
            }
        }

        return ids;
    }

    /**
     * Get unread count
     * @param messageUnreadDTO
     * @return
     */
    public int getUnreadCount(MessageUnreadDTO messageUnreadDTO) {
        return messageMapper.getUnreadCount(messageUnreadDTO);
    }

    /**
     * Get three type of notifications
     * @param userId
     * @return
     */
    public List<NotificationVO> getNotifications(Long userId) {
        List<NotificationVO> notificationVOList = new ArrayList<>();

        // Get comment notice
        Message message = messageMapper.getLatestNotice(userId, EntityTypeConstant.TOPIC_COMMENT);

        if (message != null) {
            Map<String, Object> data = JSONObject.parseObject(message.getContent());
            Integer userIntId = (Integer) data.get("userId");
            User user = userService.findUserById(userIntId.longValue());
            String username = user.getUsername();
            int count = messageMapper.getNoticeCount(user.getId(), EntityTypeConstant.TOPIC_COMMENT);
            int unread = messageMapper.getNoticeUnreadCount(user.getId(), EntityTypeConstant.TOPIC_COMMENT);

            NotificationVO notificationVO = NotificationVO.builder()
                    .type(EntityTypeConstant.TOPIC_COMMENT)
                    .createTime(message.getCreateTime())
                    .username(username)
                    .entityType((Integer) data.get("entityType"))
                    .entityId(((Integer) data.get("entityId")).longValue())
                    .postId(((Integer) data.get("postId")).longValue())
                    .count(count)
                    .unread(unread)
                    .build();

            notificationVOList.add(notificationVO);
        }


        // Get like notice
        message = messageMapper.getLatestNotice(userId, EntityTypeConstant.TOPIC_LIKE);

        if (message != null) {
            Map<String, Object> data = JSONObject.parseObject(message.getContent());
            Integer userIntId = (Integer) data.get("userId");
            User user = userService.findUserById(userIntId.longValue());
            String username = user.getUsername();
            int count = messageMapper.getNoticeCount(user.getId(), EntityTypeConstant.TOPIC_LIKE);
            int unread = messageMapper.getNoticeUnreadCount(user.getId(), EntityTypeConstant.TOPIC_LIKE);

            NotificationVO notificationVO = NotificationVO.builder()
                    .type(EntityTypeConstant.TOPIC_LIKE)
                    .createTime(message.getCreateTime())
                    .username(username)
                    .entityType((Integer) data.get("entityType"))
                    .entityId(((Integer) data.get("entityId")).longValue())
                    .postId(((Integer) data.get("postId")).longValue())
                    .count(count)
                    .unread(unread)
                    .build();

            notificationVOList.add(notificationVO);
        }

        // Get follow notice
        message = messageMapper.getLatestNotice(userId, EntityTypeConstant.TOPIC_FOLLOW);

        if (message != null) {
            Map<String, Object> data = JSONObject.parseObject(message.getContent());
            Integer userIntId = (Integer) data.get("userId");
            User user = userService.findUserById(userIntId.longValue());
            String username = user.getUsername();
            int count = messageMapper.getNoticeCount(user.getId(), EntityTypeConstant.TOPIC_FOLLOW);
            int unread = messageMapper.getNoticeUnreadCount(user.getId(), EntityTypeConstant.TOPIC_FOLLOW);

            NotificationVO notificationVO = NotificationVO.builder()
                    .type(EntityTypeConstant.TOPIC_FOLLOW)
                    .createTime(message.getCreateTime())
                    .username(username)
                    .entityType((Integer) data.get("entityType"))
                    .entityId(((Integer) data.get("entityId")).longValue())
                    .count(count)
                    .unread(unread)
                    .build();

            notificationVOList.add(notificationVO);
        }

        return notificationVOList;
    }
}
