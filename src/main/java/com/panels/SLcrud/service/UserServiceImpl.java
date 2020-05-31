package com.panels.SLcrud.service;

import com.panels.SLcrud.model.Role;
import com.panels.SLcrud.model.User;
import com.panels.SLcrud.repo.AccountRepository;
import com.panels.SLcrud.repo.RoleRepository;
import com.panels.SLcrud.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    private AccountRepository accountRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder,
                       AccountRepository accountRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.accountRepository = accountRepository;
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


    @Override
    public void payToAccount(Long id, double budget) {
        User user = userRepository.findUserById(id);
        user.setAccBudget(user.getAccBudget() + budget);
        userRepository.save(user);
    }

    @Override
    public void removeFromAccount(Long id, double budget) {
        User user = userRepository.findUserById(id);
        if (user.getAccBudget() < budget)
        {
            throw  new RuntimeException("Below 0");
        }
        else {
            user.setAccBudget(user.getAccBudget() - budget);
        }
        userRepository.save(user);
    }

    @Override
    public void transfer(Long id, Long userIdDestination, double amount) {
        if(id.equals(userIdDestination)) {
            throw new RuntimeException(
                    "Impossible operation: account id must be different");
        } else {
            payToAccount(userIdDestination, amount);
            removeFromAccount(id, amount);
        }
    }
}


