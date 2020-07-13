package com.costrategix.chat.controller;

import com.costrategix.chat.config.JwtTokenUtil;
import com.costrategix.chat.model.Message;
import com.costrategix.chat.model.User;
import com.costrategix.chat.service.MessageService;
import com.costrategix.chat.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/api/chat")
public class MessageController {

    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    private UserService userService;
    @Autowired
    private MessageService messageService;

    @RequestMapping(value = "/send-message/", method = RequestMethod.POST)
    public Message sendMessage(HttpServletRequest request, @RequestBody Message message) {
        final String requestTokenHeader = request.getHeader("Authorization");
        String jwtToken = requestTokenHeader.substring(7);
        String username = this.jwtTokenUtil.getUsernameFromToken(jwtToken);
        User user = this.userService.getUserDetailsByUsername(username);
        long fromId = user.getId();
        return this.messageService.saveMessage(message, fromId);
    }
}
