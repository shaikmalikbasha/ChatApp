package com.costrategix.chat.service;

import com.costrategix.chat.dto.MessageHistoryDto;
import com.costrategix.chat.dto.UserDto;
import com.costrategix.chat.exception.MessageException;
import com.costrategix.chat.model.Message;
import com.costrategix.chat.model.MessageAttachment;
import com.costrategix.chat.model.MessageRecipients;
import com.costrategix.chat.model.User;
import com.costrategix.chat.repository.MessageAttachmentRepository;
import com.costrategix.chat.repository.MessageRecipientRepository;
import com.costrategix.chat.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Service
public class MessageService {
    private static final Logger logger = LoggerFactory.getLogger(MessageService.class);

    private MessageRepository messageRepository;
    private MessageRecipientRepository messageRecipientRepository;
    private MessageAttachmentRepository messageAttachmentRepository;
    private FileStorageService fileStorageService;
    private UserService userService;

    @Autowired
    public MessageService(MessageRepository messageRepository, MessageRecipientRepository messageRecipientRepository, MessageAttachmentRepository messageAttachmentRepository, FileStorageService fileStorageService, UserService userService) {
        this.messageRepository = messageRepository;
        this.messageRecipientRepository = messageRecipientRepository;
        this.messageAttachmentRepository = messageAttachmentRepository;
        this.fileStorageService = fileStorageService;
        this.userService = userService;
    }

    public Message saveMessage(Message message, long fromId, long[] recipients, MultipartFile[] files) throws MessageException {
        Message newMessage = new Message(); // we can use constructor here but to validate in future we used getters and setters
        newMessage.setSubject(message.getSubject());
        newMessage.setContent(message.getContent());
        newMessage.setFromId(fromId);
        newMessage.setThreadId(message.getThreadId());
        newMessage = this.messageRepository.save(newMessage);
        if (newMessage.getId() != 0) {
            for (int i = 0; i < recipients.length; i++)
                this.saveMessageRecipientData(newMessage.getId(), recipients[i]);
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

    public void uploadAttachment(MultipartFile file, long messageId) throws MessageException {
        if (file == null) {
            logger.info("For this message (" + "/" + ") no attachment is added.");
            throw new MessageException("File is not attached");
        }
        String fileName = fileStorageService.storeFile(file);
        MessageAttachment attachment = this.saveAttachment(messageId, fileName);
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

    public List<Message> getSearchResultByQuery(String query) {
        return this.messageRepository.getMessageHistoryBySearch(query);
    }

    public ResponseEntity<?> getSentMessagesByUser(User user) {
        List<MessageHistoryDto> sentMessages = this.messageRepository.getSentParentMessages(user.getId());
        return new ResponseEntity<>(this.getChildMessages(user, sentMessages), HttpStatus.OK);
    }

    public ResponseEntity<?> getRecievedMessagesByUser(User user) {
        List<MessageHistoryDto> recievedMessages = this.messageRepository.getRecievedParentMessages(user.getId());
        return new ResponseEntity<>(this.getChildMessages(user, recievedMessages), HttpStatus.OK);
    }

    public List<Object> getChildMessages(User user, List<MessageHistoryDto> messages) {
        List<Object> response = new ArrayList<>();
        Iterator iterator = messages.iterator();
        while (iterator.hasNext()) {
            MessageHistoryDto message = (MessageHistoryDto) iterator.next();
            message.setRecipients(this.getRecipientsIdsByMessageId(message.getMessageId()));
            List<MessageHistoryDto> childMessages = this.messageRepository.getRepliedMessages(message.getMessageId(), user.getId());
            Iterator i = childMessages.iterator();
            List<MessageHistoryDto> childMessagesContainer = new ArrayList<>();
            while (i.hasNext()) {
                MessageHistoryDto messageHistoryDto = (MessageHistoryDto) i.next();
                messageHistoryDto.setRecipients(this.getRecipientsIdsByMessageId(messageHistoryDto.getMessageId()));
                childMessagesContainer.add(messageHistoryDto);
            }
            message.setRepliedMessages(childMessagesContainer);
            response.add(message);
        }
        return response;
    }

    public List<UserDto> getRecipientsIdsByMessageId(long messageId) {
        List<Long> recipientsIds = this.messageRepository.getRecipientsIdsByMessageId(messageId);
        return this.userService.getUserDetailsByRecipientsIds(recipientsIds);
    }
}

