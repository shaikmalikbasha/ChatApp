package com.costrategix.chat.controller;

import com.costrategix.chat.model.Message;
import com.costrategix.chat.model.User;
import com.costrategix.chat.service.FileStorageService;
import com.costrategix.chat.service.MessageService;
import com.costrategix.chat.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "/api/chat")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(MessageController.class);

    private UserService userService;
    private MessageService messageService;
    private FileStorageService fileStorageService;

    @Autowired
    public MessageController(UserService userService, MessageService messageService, FileStorageService fileStorageService) {
        this.userService = userService;
        this.messageService = messageService;
        this.fileStorageService = fileStorageService;
    }

    @RequestMapping(value = "/send-message/{recipientId}", method = RequestMethod.POST)
    public Message sendMessage(@RequestParam(value = "files", required = false) MultipartFile[] files, HttpServletRequest request, Message message, @PathVariable long recipientId) throws Exception {
        final String requestTokenHeader = request.getHeader("Authorization");
        User user = this.userService.getUserByToken(requestTokenHeader);
        long fromId = user.getId();
        return this.messageService.saveMessage(message, fromId, recipientId, files);
    }

    @RequestMapping(value = "/get-recipients", method = RequestMethod.GET)
    public ResponseEntity<?> getRecipientNamesById(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        User user = this.userService.getUserByToken(requestTokenHeader);
        return new ResponseEntity<>(this.userService.getRecipientsById(user.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "update-read-status/{messageId}")
    public boolean updateReadStatusByMessageId(@PathVariable long messageId) {
        return this.messageService.updateReadStatusByMessageId(messageId);
    }

    @RequestMapping(value = "/messages", method = RequestMethod.GET)
    public ResponseEntity<?> getSentMessagesByUserId(HttpServletRequest request) {
        final String requestTokenHeader = request.getHeader("Authorization");
        User user = this.userService.getUserByToken(requestTokenHeader);
        return new ResponseEntity<>(this.messageService.getMessageHistoryByUserId(user.getId()), HttpStatus.OK);
    }

    @RequestMapping(value = "/messages/{recipientId}", method = RequestMethod.GET)
    public ResponseEntity<?> getRecievedMessagesByUserId(@PathVariable long recipientId) {
        return new ResponseEntity<>(this.messageService.getMessageHistoryByUserId(recipientId), HttpStatus.OK);
    }

    @RequestMapping(value = "/messages/id/{messageId}", method = RequestMethod.GET)
    public ResponseEntity<?> getMessageById(@PathVariable long messageId) {
        return new ResponseEntity<>(this.messageService.getMessageById(messageId), HttpStatus.OK);
    }

    @RequestMapping(value = "/download/{fileName}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = fileStorageService.loadFileAsResource(fileName);
        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
            logger.info("Could not determine file type.");
        }
        // Fallback to the default content type if type could not be determined
        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

//    @RequestMapping("/reply-message/{recipientId}")
//    public ResponseEntity<?> addReplyToMessageByMessageId(@RequestParam(value = "files", required = false) MultipartFile[] files, HttpServletRequest request, Message message, @PathVariable long recipientId) throws Exception {
//        final String requestTokenHeader = request.getHeader("Authorization");
//        User user = this.userService.getUserByToken(requestTokenHeader);
//        message.setThreadId(messageId);
//        return new ResponseEntity<>(this.messageService.saveMessage(), HttpStatus.OK);
//    }

    @RequestMapping(value = "/search")
    public ResponseEntity<?> getSearchResultsByQuery(@RequestParam String query) {
        return new ResponseEntity<>(this.messageService.getSearchResultByQuery(query), HttpStatus.OK);
    }
}
