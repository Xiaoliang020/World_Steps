package com.travel.service;

import com.travel.dto.*;
import com.travel.result.PageResult;
import com.travel.vo.NotificationVO;

import java.util.List;

public interface MessageService {

    /**
     * Save a new message
     * @param messageDTO
     * @return
     */
    void save(MessageDTO messageDTO);

    /**
     * Set status to read
     * @param conversationId
     */
    void setRead(String conversationId);

    /**
     * Message Page query
     * @param messagePageQueryDTO
     * @return
     */
    PageResult pageQuery(MessagePageQueryDTO messagePageQueryDTO);

    /**
     * Conversation Page query
     * @param messageConversationPageQueryDTO
     * @return
     */
    PageResult conversationPageQuery(MessageConversationPageQueryDTO messageConversationPageQueryDTO);

    /**
     * Get unread count
     * @param messageUnreadDTO
     * @return
     */
    int getUnreadCount(MessageUnreadDTO messageUnreadDTO);

    /**
     * Get three type of notifications
     * @param userId
     * @return
     */
    List<NotificationVO> getNotifications(Long userId);

    /**
     * Page query notifications under a topic
     * @param noticePageQueryDTO
     * @return
     */
    PageResult pageQueryNotification(NoticePageQueryDTO noticePageQueryDTO);
}
