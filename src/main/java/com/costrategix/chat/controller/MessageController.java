package com.costrategix.chat.controller;

import com.costrategix.chat.model.Message;
import com.costrategix.chat.model.User;
import com.costrategix.chat.service.MessageService;
import com.costrategix.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/chat")
public class MessageController {

    private UserService userService;
    private MessageService messageService;

    @Autowired
    public MessageController(UserService userService, MessageService messageService) {
        this.userService = userService;
        this.messageService = messageService;
    }

    @RequestMapping(value = "/send-message/{recipientId}", method = RequestMethod.POST)
    public Message sendMessage(HttpServletRequest request, @RequestBody Message message, @PathVariable long recipientId) {
        final String requestTokenHeader = request.getHeader("Authorization");
        User user = this.userService.getUserByToken(requestTokenHeader);
        long fromId = user.getId();
        return this.messageService.saveMessage(message, fromId, recipientId);
    }

    @RequestMapping(value = "/get-recipients", method = RequestMethod.GET)
    public ResponseEntity<?> getRecipientNamesById(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        User user = this.userService.getUserByToken(requestTokenHeader);
        return new ResponseEntity<>(this.userService.getRecipientsById(user.getId()), HttpStatus.OK);
    }
}
