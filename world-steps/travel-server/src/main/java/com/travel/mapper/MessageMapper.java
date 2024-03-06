package com.travel.mapper;

import com.github.pagehelper.Page;
import com.travel.dto.MessageConversationPageQueryDTO;
import com.travel.dto.MessagePageQueryDTO;
import com.travel.dto.MessageUnreadDTO;
import com.travel.dto.NoticePageQueryDTO;
import com.travel.entity.Message;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageMapper {

    /**
     * Insert a new message
     * @param message
     */
    @Insert("insert into message (from_id, to_id, conversation_id, content, status, create_time) " +
            "values" +
            "(#{fromId}, #{toId}, #{conversationId}, #{content}, #{status}, #{createTime})")
    void insert(Message message);

    /**
     * Update message status
     * @param ids
     * @param status
     */
    void update(List<Long> ids, int status);

    /**
     * Message page query
     * @param messagePageQueryDTO
     * @return
     */
    Page<Message> pageQuery(MessagePageQueryDTO messagePageQueryDTO);


    /**
     * Message conversation page query
     * @param messageConversationPageQueryDTO
     * @return
     */
    Page<Message> conversationPageQuery(MessageConversationPageQueryDTO messageConversationPageQueryDTO);

    /**
     * Get count of unread messages
     * @param messageUnreadDTO
     * @return
     */
    int getUnreadCount(MessageUnreadDTO messageUnreadDTO);

    /**
     * Get the latest notice of a topic
     * @param userId
     * @param topic
     * @return
     */
    Message getLatestNotice(Long userId, String topic);

    /**
     * Get count of notice of a topic
     * @param userId
     * @param topic
     * @return
     */
    int getNoticeCount(Long userId, String topic);

    /**
     * Get count of unread notice of a topic
     * @param userId
     * @param topic
     * @return
     */
    int getNoticeUnreadCount(Long userId, String topic);

    /**
     * Page query notifications under a topic
     */
    Page<Message> pageQueryNotice(NoticePageQueryDTO noticePageQueryDTO);

}
