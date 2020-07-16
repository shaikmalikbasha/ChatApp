package com.costrategix.chat.service;

import com.costrategix.chat.controller.FileController;
import com.costrategix.chat.dto.MessageHistoryDto;
import com.costrategix.chat.exception.MessageException;
import com.costrategix.chat.model.Message;
import com.costrategix.chat.model.MessageAttachment;
import com.costrategix.chat.model.MessageRecipients;
import com.costrategix.chat.model.UploadFileResponse;
import com.costrategix.chat.repository.MessageAttachmentRepository;
import com.costrategix.chat.repository.MessageRecipientRepository;
import com.costrategix.chat.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private MessageRepository messageRepository;
    private MessageRecipientRepository messageRecipientRepository;
    private MessageAttachmentRepository messageAttachmentRepository;
    private FileStorageService fileStorageService;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageRecipientRepository messageRecipientRepository, MessageAttachmentRepository messageAttachmentRepository, FileStorageService fileStorageService) {
        this.messageRepository = messageRepository;
        this.messageRecipientRepository = messageRecipientRepository;
        this.messageAttachmentRepository = messageAttachmentRepository;
        this.fileStorageService = fileStorageService;
    }

    public Message saveMessage(Message message, long fromId, long recipientId, MultipartFile[] files) throws MessageException {
        Message newMessage = new Message(); // we can use constructor here but to validate in future we used getters and setters
        newMessage.setSubject(message.getSubject());
        newMessage.setContent(message.getContent());
        newMessage.setFromId(fromId);
        newMessage.setThreadId(message.getThreadId());
        newMessage = this.messageRepository.save(newMessage);
        if (newMessage.getId() != 0) {
            this.saveMessageRecipientData(newMessage.getId(), recipientId);
            this.uploadMultipleAttachments(files, newMessage.getId());
        }
        return newMessage;
    }

    public MessageRecipients saveMessageRecipientData(long messageId, long toId) {
        MessageRecipients result = new MessageRecipients();
        result.setMessageId(messageId);
        result.setRecipientId(toId);
        return this.messageRecipientRepository.save(result);
    }

    public MessageAttachment saveAttachment(long messageId, String fileName) {
        MessageAttachment attachment = new MessageAttachment();
        attachment.setMessageId(messageId);
        attachment.setFileName(fileName);
        return this.messageAttachmentRepository.save(attachment);
    }

    public MessageHistoryDto getMessageById(long messageId) {
        return this.messageRepository.getMessageByMessageId(messageId);
    }

    public boolean updateReadStatusByMessageId(long messageId) {
        return (this.messageRepository.updateMessageByMessageId(messageId) > 0) ? true : false;
    }

    public List<MessageHistoryDto> getMessageHistoryByUserId(long userId) {
        return this.messageRepository.getMessageHistoryByUserId(userId);
    }

    public void uploadAttachment(MultipartFile file, long messageId) throws MessageException {
        if (file == null) {
            logger.info("For this message (" + "/" + ") no attachment is added.");
            throw new MessageException("File is not attached");
        }
        String fileName = fileStorageService.storeFile(file);
        MessageAttachment attachment = this.saveAttachment(messageId, fileName);
        System.out.println("FileName: " + fileName);
    }

    public void uploadMultipleAttachments(@RequestParam(value = "files", required = false) MultipartFile[] files, long messageId) throws MessageException {
        for (MultipartFile file : Arrays.asList(files)) {
            this.uploadAttachment(file, messageId);
        }
    }

    public ResponseEntity<Resource> downloadFile(HttpServletRequest request, String fileName) {
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

//    public List<Message> getSearchResultByQuery(String query, long userId) {
//        List<Message> res = this.messageRepository.getMessageHistoryBySearch(query, userId);
//        return res;
//    }
}
