package com.travel.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
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
import com.travel.vo.MessageVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private UserMapper userMapper;

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
            messageVO.setAvatar(userMapper.getAvatar(targetId));
            messageVO.setUsername(userMapper.getUsernameById(targetId));
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
            messageVO.setAvatar(userMapper.getAvatar(message.getFromId()));
            messageVO.setUsername(userMapper.getUsernameById(message.getFromId()));
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
}
