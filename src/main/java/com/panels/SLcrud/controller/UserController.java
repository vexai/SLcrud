package com.panels.SLcrud.controller;


import antlr.debug.MessageAdapter;
import com.panels.SLcrud.model.Account;
import com.panels.SLcrud.model.Message;
import com.panels.SLcrud.model.Operation;
import com.panels.SLcrud.model.User;
import com.panels.SLcrud.repo.AccountRepository;
import com.panels.SLcrud.repo.OperationRepository;
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

    private OperationRepository operationRepository;

    public UserController(AccountRepository accountRepository,
                          OperationRepository operationRepository) {
        this.accountRepository = accountRepository;
        this.operationRepository = operationRepository;
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
        List<Operation> allUserOperations = this.userService.selectAllUserOperations(user.getId());
        modelAndView.addObject("allOperations", allUserOperations);
        List<Message> allUsersMessages = this.userService.selectAllMessages(user.getId());
        modelAndView.addObject("allMessages",allUsersMessages);
        modelAndView.setViewName("user/home");
        return modelAndView;
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        this.userService.deleteUser(id);
        return "redirect:/admin/home?deletedId" + id;
    }

    @GetMapping("user/sentMessage/{id}")
    public  String sendTextMessage(@PathVariable Long id,
                                   Long userIdDestination,
                                   String text,
                                   String messageType,
                                   Long userIdOrigin,
                                   Long did) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userId", user.getId());
        try
        {
            this.userService.send(id,userIdDestination,text,messageType,did,userIdOrigin);
        } catch (Exception e){
            return "redirect:/user/home?sentMessage?ERRORERRORERROR=" + e.getMessage();
        }
            return "redirect:/user/home?sentMessage=" + id;
    }


    @GetMapping("/user/savedAccountOperation/{id}")
    public String saveAccountOperation(@PathVariable Long id,
                                       String operationType,
                                       @RequestParam double operationBudget,
                                       Long oid,
                                       Long did) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userId", user.getId());
        try {
            if (operationType.equals("DEPOSIT"))
            {
                this.userService.depostitToAccount(id ,operationBudget, operationType, oid, did);
            }
            else if (operationType.equals("WITHDRAWAL"))
            {
                this.userService.withdrawalFromAccount(id, operationBudget, operationType, did, oid );
            }

        } catch (Exception e) {
            return "redirect:/user/home?savedAccountOperation?ERRORERRORERROR=" + id + e.getMessage();
        }
        return "redirect:/user/home?savedAccountOperation=" + id;
    }

    @GetMapping("/user/operationTransfered/{id}")
    public String transferToAccount(@PathVariable Long id,
                                    Long userIdOrigin,
                                    Long userIdDestination,
                                    @RequestParam double operationBudget,
                                    String operationType,
                                    Long oid,
                                    Long did) {
        ModelAndView modelAndView = new ModelAndView();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUserName(auth.getName());
        modelAndView.addObject("userId", user.getId());
        try {

            this.userService.transfer(id,userIdDestination, operationBudget, operationType, did, userIdOrigin);

        } catch (Exception e) {

            return "redirect:/user/home?operationERROR=" + id + e.getMessage();

        }
        return "redirect:/user/home?operationSuccess=" + id;
    }
}

