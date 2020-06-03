package com.panels.SLcrud.service;

import com.panels.SLcrud.model.*;
import com.panels.SLcrud.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AccountRepository accountRepository;
    private MessageRepository messageRepository;
    private OperationRepository operationRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       AccountRepository accountRepository,
                           OperationRepository operationRepository,
                           MessageRepository messageRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
        this.messageRepository = messageRepository;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findUserByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public User findUserById(Long id) {
        return userRepository.findUserById(id);
    }


    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(true);
        Role userRole = roleRepository.findByRole("ROLE_USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        user.setAccBudget(1000L);
        return userRepository.save(user);
    }

    public List<User> selectAllUsers() {
        return this.userRepository.findAllByRoles_Role("ROLE_USER");
    }

    public void deleteUser(Long id) {
        this.userRepository.deleteById(id);
    }

    public List<Operation> selectAllOperations() {
        return this.operationRepository.findAllByOp("op");
    }

    public List<Operation> selectAllUserOperations(Long id) {
        User user = userRepository.findUserById(id);
        return this.operationRepository.findAllByUserId(user.getId());
    }

    public List<Message> selectAllMessages(Long id) {
        User user = userRepository.findUserById(id);
        return this.messageRepository.findAllByUserId(user.getId());
    }

    public void depostitToAccount(Long id, double budget, String op, Long did, Long oid) {
        User user = userRepository.findUserById(id);
        Operation operation = new Operation(new Date(), budget, user, op, did, oid);
        operationRepository.save(operation);
        user.setAccBudget(user.getAccBudget() + budget);
        userRepository.save(user);
    }

    public void withdrawalFromAccount(Long id, double budget, String op, Long did, Long oid) {
        User user = userRepository.findUserById(id);
        if (user.getAccBudget() < budget)
        {
            throw  new RuntimeException("Below 0");
        }
        else {
            user.setAccBudget(user.getAccBudget() - budget);
        }
        Operation operation = new Operation(new Date(), budget, user, op, did, oid);
        operationRepository.save(operation);
        userRepository.save(user);
    }


//    @Override
    public void payToAccount(Long id, double budget, String op, Long userIdDestination, Long userIdOrigin) {
        User user = userRepository.findUserById(id);
        Operation operation = new Operation(new Date(), budget, user, "PAYMENT", userIdDestination, userIdOrigin);
//        operation.setDid(user.getId());
        operationRepository.save(operation);
        user.setAccBudget(user.getAccBudget() + budget);
        userRepository.save(user);
    }

//    @Override
    public void removeFromAccount(Long id, double budget, String op, Long userIdDestination, Long userIdOrigin) {
        User user = userRepository.findUserById(id);
        if (user.getAccBudget() < budget)
        {
            throw  new RuntimeException("Below 0");
        }
        else {
            user.setAccBudget(user.getAccBudget() - budget);
        }
        Operation operation = new Operation(new Date(), budget, user, "TRANSFER", userIdDestination, userIdOrigin);
//        operation.setOid(user.getId());
        operationRepository.save(operation);
        userRepository.save(user);
    }

//    @Override
    public void transfer(Long userIdOrigin,Long userIdDestination, double amount, String op, Long did, Long oid) {
        if(userIdOrigin.equals(userIdDestination)) {
            throw new RuntimeException(
                    "Impossible operation: account id must be different");
        } else {
            payToAccount(userIdDestination, amount, "PAYMENT", userIdDestination, userIdOrigin);
            removeFromAccount(userIdOrigin, amount, "TRANSFER", userIdDestination, userIdOrigin);
        }
    }

    public void sentMessage(Long id, String text, String messageType, Long userIdDestination, Long userIdOrigin) {
        User user = userRepository.findUserById(id);
        Message message = new Message(new Date(),messageType, text, userIdOrigin, userIdDestination, user);
        messageRepository.save(message);
    }

    public void receivedMessage(Long id, String text, String messageType, Long userIdDestination, Long userIdOrigin) {
        User user = userRepository.findUserById(id);
        Message message = new Message(new Date(),messageType, text, userIdOrigin, userIdDestination, user);
        messageRepository.save(message);
    }

    public void send(Long userIdOrigin, Long userIdDestination, String text, String messageType,Long did, Long oid){
        if(userIdOrigin.equals(userIdDestination)) {
            throw new RuntimeException(
                    "Impossible operation: account id must be different");
        } else {
            sentMessage(userIdOrigin, text,"SENT",userIdDestination, userIdOrigin);
            receivedMessage(userIdDestination, text,"RECEIVED", userIdDestination, userIdOrigin);
        }
    }
}


