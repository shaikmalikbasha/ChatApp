package com.costrategix.chat.service;

import com.costrategix.chat.config.JwtTokenUtil;
import com.costrategix.chat.dto.UserDto;
import com.costrategix.chat.model.User;
import com.costrategix.chat.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = this.userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username : " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public User getUserByToken(String token) {
        String jwtToken = token.substring(7);
        String username = this.jwtTokenUtil.getUsernameFromToken(jwtToken);
        return this.getUserDetailsByUsername(username);
    }

    public User getUserDetailsByUsername(String username) {
        return this.userRepository.findByUsername(username);
    }

    public User save(User user) {
        User newUser = new User();
        newUser.setName(user.getName());
        newUser.setUsername(user.getUsername());
        newUser.setPassword(this.bcryptEncoder.encode(user.getPassword()));
        return this.userRepository.save(newUser);
    }

    public List<UserDto> getRecipientsById(long fromId) {
        return this.userRepository.getRecipientsById(fromId);
    }

    public List<UserDto> getUsersBySearchQuery(@RequestParam String q) {
        return this.userRepository.getUsersBySearchQuery(q);
    }
}
