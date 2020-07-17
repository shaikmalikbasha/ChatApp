package com.costrategix.chat.repository;

import com.costrategix.chat.dto.UserDto;
import com.costrategix.chat.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    @Query("SELECT DISTINCT new com.costrategix.chat.dto.UserDto(u.id, u.name, u.username) FROM User u, Message m, MessageRecipients mr WHERE m.id = mr.messageId AND mr.recipientId = u.id AND m.fromId = ?1")
    List<UserDto> getRecipientsById(long fromId);

    @Query("SELECT DISTINCT new com.costrategix.chat.dto.UserDto(u.id, u.name, u.username) FROM User u WHERE u.name LIKE %:q%")
    List<UserDto> getUsersBySearchQuery(String q);
}
