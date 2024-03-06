package com.travel.controller.user;

import com.travel.context.BaseContext;
import com.travel.dto.MessageConversationPageQueryDTO;
import com.travel.dto.MessageDTO;
import com.travel.dto.MessagePageQueryDTO;
import com.travel.dto.NoticePageQueryDTO;
import com.travel.result.PageResult;
import com.travel.result.Result;
import com.travel.service.MessageService;
import com.travel.vo.NotificationVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/message")
@Slf4j
@Api(tags = "Message related interfaces")
public class MessageController {
    @Autowired
    private MessageService messageService;

    /**
     * Save a message
     * @param messageDTO
     * @return
     */
    @PostMapping
    @ApiOperation("Message save")
    public Result save(@RequestBody MessageDTO messageDTO) {
        log.info("发送私信：{}", messageDTO);
        messageService.save(messageDTO);
        return Result.success();
    }

    /**
     * Get message by page
     * @param messagePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("Message page query")
    public Result<PageResult> page(MessagePageQueryDTO messagePageQueryDTO) {
        log.info("分页查询私信列表：{}", messagePageQueryDTO);
        PageResult pageResult= messageService.pageQuery(messagePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Get conversation by page
     * @param messageConversationPageQueryDTO
     * @return
     */
    @GetMapping("/conversation")
    @ApiOperation("Conversation page query")
    public Result<PageResult> pageConversation(MessageConversationPageQueryDTO messageConversationPageQueryDTO) {
        log.info("分页查询会话历史列表：{}", messageConversationPageQueryDTO);
        PageResult pageResult= messageService.conversationPageQuery(messageConversationPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * Get notifications
     * @return
     */
    @GetMapping("/notice")
    @ApiOperation("Get notifications")
    public Result<List<NotificationVO>> notification(){
        log.info("查询通知：{}", BaseContext.getCurrentId());
        List<NotificationVO> notifications = messageService.getNotifications(BaseContext.getCurrentId());
        return Result.success(notifications);
    }

    /**
     * Page query notifications
     * @return
     */
    @GetMapping("/notifications")
    @ApiOperation("Page query notifications under a topic")
    public Result<PageResult> pageNotifications(NoticePageQueryDTO noticePageQueryDTO) {
        log.info("分页查询主题通知：{}", noticePageQueryDTO);
        PageResult pageResult = messageService.pageQueryNotification(noticePageQueryDTO);
        return Result.success(pageResult);
    }
}
