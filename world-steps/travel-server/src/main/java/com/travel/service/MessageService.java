package com.travel.service;

import com.travel.dto.MessageConversationPageQueryDTO;
import com.travel.dto.MessageDTO;
import com.travel.dto.MessagePageQueryDTO;
import com.travel.dto.MessageUnreadDTO;
import com.travel.result.PageResult;

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
}
