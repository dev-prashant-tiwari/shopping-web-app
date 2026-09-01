package com.userService.service;

import com.userService.constants.ApplicationConstants;
import com.userService.model.User;
import com.userService.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@org.springframework.stereotype.Service
public class CustomerService<T> {

    UserRepository userRepository;
    @Autowired
    public CustomerService(UserRepository userRepository){
     this.userRepository = userRepository;
    }
    @Autowired
    PasswordEncoder passwordEncoder;
    public ResponseEntity<Object> createUser(User user){
        try {
            log.info("creating the user {}",user.toString());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User result = userRepository.save(user);
            return new ResponseEntity<>(result, HttpStatus.CREATED);
        }catch (Exception ex){
            log.error(ApplicationConstants.GENEXCEPTIONMSG+" "+ex.getMessage());
            return new ResponseEntity<>(ApplicationConstants.GENEXCEPTIONMSG, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> getUsers(){
        try{
            List<User> users = userRepository.findAll();
            return new ResponseEntity<>(users,HttpStatus.OK);
        }catch (Exception ex){
            log.error(ApplicationConstants.GENEXCEPTIONMSG+" "+ex.getMessage());
            return new ResponseEntity<>(ApplicationConstants.GENEXCEPTIONMSG,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> updateUser(Long id, User user) {
        try{
            log.info("updating user with id {} and user: {}",id,user);
            Optional<User> existingUserObj = userRepository.findById(id);
            if(existingUserObj.isPresent()){
               User existingUser = existingUserObj.get();
               User result = updateNonNullFields(existingUser,user);
               return new ResponseEntity<>(result,HttpStatus.OK);
            }else{
                log.warn("used does not exist with provided id");
                return new ResponseEntity<>("user could not be updated",HttpStatus.NOT_FOUND);
            }

        }catch (Exception ex){
            log.error("exception occurred while updating the user with id {}",id);
            return new ResponseEntity<>(ApplicationConstants.GENEXCEPTIONMSG+ex,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private User updateNonNullFields(User existingUser, User user) {
        if(Objects.nonNull(user.getUsername())){
            existingUser.setUsername(user.getUsername());
        }
        if(Objects.nonNull(user.getEmail())){
            existingUser.setEmail(user.getEmail());
        }
        if(Objects.nonNull(user.getPassword())){
            existingUser.setPassword(user.getPassword());
        }
        if(Objects.nonNull(user.getMobile())){
            existingUser.setMobile(user.getMobile());
        }
        return userRepository.save(existingUser);
    }

    public ResponseEntity<T> getUser(Long id) {
        try {
            log.info("getting user with id {}",id);
            Optional<User> user = userRepository.findById(id);
            if(user.isPresent()){
                return new ResponseEntity<T>((T) user.get(),HttpStatus.OK);
            }
            else{
                log.warn("user does not exist with id {}",id);
                return new ResponseEntity<T>((T) "user does not exist",HttpStatus.NOT_FOUND);
            }
        }catch (Exception ex){
            log.error(ApplicationConstants.GENEXCEPTIONMSG, HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<T>((T)(ApplicationConstants.GENEXCEPTIONMSG+ex),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Object> deleteUser(Long id) {
        try {
            log.info("deleting the user with id {}",id);
            userRepository.deleteById(id);
            log.warn("user deleted successfully with id {}",id);
            return new ResponseEntity<>("user deleted successfully",HttpStatus.OK);

        }catch (Exception ex){
            log.error(ApplicationConstants.GENEXCEPTIONMSG, HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(ApplicationConstants.GENEXCEPTIONMSG+ex,HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
