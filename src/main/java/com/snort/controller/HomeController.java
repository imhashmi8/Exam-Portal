package com.snort.controller;

import com.snort.repository.UserRepository;
import com.snort.entities.User;
import com.snort.helper.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class HomeController {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/home")
    public String home(Model model){
        model.addAttribute("title","Home Smart-Contact-Manager");
        return "home";
    }
    @GetMapping("/about")
    public String about(Model model){
        model.addAttribute("title","Home Smart-Contact-Manager");
        return "about";
    }
    @GetMapping("/signup")
    public String signUp(Model model){
        model.addAttribute("title","Home Smart-Contact-Manager");
        model.addAttribute("user",new User());
        return "signup";
    }
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model, BindingResult bindingResult, HttpSession session){
       try{
           if (!agreement){
               System.out.println("You have not agreed the terms and condition.");
               throw new Exception("You have not agreed the terms and condition.");
           }
           if (bindingResult.hasErrors()){
               System.out.println("ERROR : "+bindingResult.toString());
               model.addAttribute("user",user);
               return "signup";
           }
           user.setRole("ROLE_USER");
           user.setEnabled(true);
           System.out.println("agreement : "+agreement);
           System.out.println("User : "+user);
           user.setImageUrl("default.png");
           user.setPassword(passwordEncoder.encode(user.getPassword()));
           User result = userRepository.save(user);
           model.addAttribute("user",new User());
           session.setAttribute("message",new Message("successfully Registered","alert-success"));
           return "signup";
       }    catch (Exception e){
           e.printStackTrace();
           model.addAttribute("user",user);
           session.setAttribute("message",new Message("something went wrong!!"+e.getMessage(),"alert-danger"));
           return "signup";
       }
//hello world
    }
    /*Handler for custom Login*/
    @GetMapping("/signin")
    public String customLogin(Model model){
        model.addAttribute("title","Login Page");
        return "login";
    }

}
