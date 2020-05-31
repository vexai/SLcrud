package com.panels.SLcrud.controller;


import com.panels.SLcrud.model.Account;
import com.panels.SLcrud.model.User;
import com.panels.SLcrud.repo.AccountRepository;
import com.panels.SLcrud.service.UserService;
import com.panels.SLcrud.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
public class UserController {

    int adminRoleId = 1;

    @Autowired
    private UserServiceImpl userService;

    private AccountRepository accountRepository;

    private UserService userServiceInterface;

    public UserController(AccountRepository accountRepository, UserService userServiceInterface) {
        this.accountRepository = accountRepository;
        this.userServiceInterface = userServiceInterface;
    }

    @GetMapping(value={"/", "/login"})
    public ModelAndView login(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("login");
        return modelAndView;
//    public List<Account> accountList() {
//        return accountRepository.findAll();
    }

//    @GetMapping(value={"/all"})
////    public List<Account> accountList() {
////        return accountRepository.findAll();
//    }

    @RequestMapping(value= {"/default"})
    public String defaultAfterLogin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        if (user.getUserName().equals("admin")) {
            return "redirect:/admin/home";
        }
        return "redirect:/user/home";
    }

    @GetMapping(value="/registration")
    public ModelAndView registration(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("registration");
        return modelAndView;
    }

    @PostMapping(value = "/registration")
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult, Account account) {
        ModelAndView modelAndView = new ModelAndView();
        User userExists = userService.findUserByUserName(user.getUserName());
        if (userExists != null) {
            bindingResult
                    .rejectValue("userName", "error.user",
                            "There is already a user registered with the user name provided");
        }
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("registration");
        } else {
//            userService.saveAccount(account);
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "User has been registered successfully");
//            modelAndView.addObject("account", new Account());
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("registration");

        }
        return modelAndView;
    }

    @GetMapping(value="/admin/home")
    public ModelAndView home(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        List<User> allUsers = this.userService.selectAllUsers();
        modelAndView.addObject("allUsers", allUsers);
        modelAndView.addObject("adminMessage","Content Available Only for Users with Admin Role");
        modelAndView.setViewName("admin/home");
        return modelAndView;
    }

    @GetMapping(value="/user/home")
    public ModelAndView userHome(){
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userName", "Welcome " + user.getUserName() + "/" + user.getName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
        modelAndView.addObject("accBudget", user.getAccBudget());
        modelAndView.addObject("userId", user.getId());
        modelAndView.addObject("userMessage","USER Role");
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return "redirect:/admin/home?deletedId" + id;
    }

    @PostMapping("/pay/{id}")
    public String pay(@PathVariable Long id, double budget, @Valid User user, Model model) {
        this.userService.payToAccount(id, budget);
        user.setId(id);
        return "redirect:/user/home?payedId" + id;
    }


    @GetMapping("/user/savedAccountOperation/{id}")
    public String saveAccountOperation(@PathVariable Long id,
                                       String operationType,
                                       @RequestParam double operationBudget) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userId", user.getId());
        try {
            if (operationType.equals("PAYMENT"))
            {
                this.userService.payToAccount(id,operationBudget);
            }
            else if (operationType.equals("WITHDRAWAL"))
            {
                this.userService.removeFromAccount(id, operationBudget);
            }

        } catch (Exception e) {
            return "redirect:/user/home?savedAccountOperation?ERRORERRORERROR=" + e.getMessage();
        }
        return "redirect:/user/home?savedAccountOperation=" + id;
    }
}

