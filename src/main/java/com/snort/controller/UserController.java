package com.snort.controller;

import com.snort.entities.*;
import com.snort.helper.Message;
import com.snort.repository.*;
import com.snort.service.QuestionService;
import com.snort.service.UserQuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private UserQuestionService userQuestionService;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private UserQuestionRepository userQuestionRepository;

    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private NoticeRepository noticeRepository;

    /*This Handler addCommonData is used to get The logged-in Username */
    @ModelAttribute
    public void addCommonData(Model model, Principal principal) {

        String userName = principal.getName();
        System.out.println("USERNAME :" + userName);

        User user = userRepository.getUserByUserName(userName);
        model.addAttribute("user", user);

    }

    /*This handler is used to  show the User Home page, */
    @RequestMapping("/index")
    public String dashboard(Model model, Principal principal) {
        model.addAttribute("title", "User Dashboard");

        return "normal/user_dashboard";
    }
    //hello

    /*This Handler is used to open the add contact page when the user clicks on /add-contact link or button*/
    @GetMapping("/add-contact")
    public String openAddContactForm(Model model) {

        model.addAttribute("title", "Add Contact");
        model.addAttribute("contact", new Contact());
        return "normal/add_contact_form";
    }

    /*This processContact handler is used to add the contact */
    @PostMapping("/process-contact")
    public String processContact(@ModelAttribute Contact contact, @RequestParam("profileImage") MultipartFile file,
                                 Principal principal, HttpSession session) {
        try {
            String name = principal.getName();
            User user = this.userRepository.getUserByUserName(name);

            if (file.isEmpty()) {
                System.out.println("File is Empty");
//                contact.setImage("contact1.png");

            } else {
                contact.setImage(file.getOriginalFilename());

                File saveFile = new ClassPathResource("static/image").getFile();

                Path path = Paths.get(saveFile.getAbsolutePath() + File.separator + file.getOriginalFilename());

                Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

                System.out.println("Image is Uploaded");
            }

            contact.setUser(user);

            user.getContacts().add(contact);
            this.userRepository.save(user);

            System.out.println("Contact :" + contact);

            System.out.println("Added to Database");

//            session.setAttribute("message", new Message("Your Contact Is Added !! Add More", "success"));

        } catch (Exception e) {
            e.printStackTrace();

//            session.setAttribute("message", new Message("Something Went Wrong !! Try Again", "danger"));
        }
        return "normal/add_contact_form";
    }

    /* Show-contact Handler is used to show the contact of the particular user */
    @GetMapping("/show-contacts")
    public String showContact(Model model, Principal principal) {
        model.addAttribute("title", "Show User Contacts");
        String userName = principal.getName();
        User user = this.userRepository.getUserByUserName(userName);
        List<Contact> contacts = this.contactRepository.findContactByUser(user.getId());
        model.addAttribute("contacts", contacts);
        return "normal/show_contacts";
    }

    /* this handler is used to delete the Contact of any Particular user that he has saved and it will redirect to the show-contact page*/
    @GetMapping("/delete/{cid}")
    public String deleteContact(@PathVariable("cid") Integer cId, Model model, HttpSession session, Principal principal) {
        System.out.println("CID :" + cId);
        Contact contact = this.contactRepository.findById(cId).get();

        System.out.println("Contact :" + contact.getCId());

        contact.setUser(null);
        this.contactRepository.delete(contact);

        User user = this.userRepository.getUserByUserName(principal.getName());

        user.getContacts().remove(contact);

        this.userRepository.save(user);

        System.out.println("Contact Deleted");
        session.setAttribute("message", new Message("Contact Deleted Successfully!!", "success"));

        return "redirect:/user/show-contacts";
    }


    //handler for profile
    @GetMapping("/profile")
    public String yourProfile(Model model) {

        model.addAttribute("title", "Profile");

        return "normal/profile";
    }

    /*This handler is used to search the particular contact */
    @GetMapping("/search")
    public ModelAndView searchBy(HttpServletRequest request) {
        ModelAndView view = new ModelAndView("normal/show_contacts");
        String optionName = request.getParameter("optionName").trim();
        String optionValue = request.getParameter("optionValue").trim();

        List<Contact> contactList = new ArrayList<>();

        switch (optionName) {
            case "name": {
                contactList = contactRepository.findByName(optionValue);
                System.out.println("contactList : " + contactList);
            }
            break;
            case "email": {
                contactList = contactRepository.findByEmail(optionValue);
                System.out.println("contactList : " + contactList);

            }
            break;
            case "phone": {
                contactList = contactRepository.findByPhone(optionValue);
                System.out.println("contactList : " + contactList);
            }
            break;
            default:
                break;
        }
        view.addObject("contactList", contactList);
        return view;
    }

    /*Handler to open a new page to go to assigned_question exams*/
    @GetMapping("/start")
    public String examPage(Model model, Principal principal) {
        String email = principal.getName();

        model.addAttribute("title", "exam section");
        model.addAttribute("email", email);

        return "normal/assigned_question";
    }

    /*here this handler will give assigned questions to the student*/
    @GetMapping("/assigned-question")
    public String getAssignedQuestion(Principal principal,
                                      @RequestParam(required = false, name = "category") String category,
                                      @RequestParam(required = false, name = "level") String level,
                                      @RequestParam(required = false, name = "setNumber") Integer setNumber,
                                      @RequestParam(defaultValue = "1") int pageNumber,
                                      Model model) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);
        /*to display the name in front-end after completed the exam*/
        String userName = user.getName();

        int userId = user.getId();
        Pageable pageable = PageRequest.of(pageNumber - 1, 1);
        Page<Question> questionPage = userQuestionService.getAssignedQuestion(userId, pageable);
        model.addAttribute("userId", userId);
        model.addAttribute("questions", questionPage);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("totalPages", questionPage.getTotalPages());
        System.out.println("UserId : " + userId + ":Page number : " + pageNumber + " :total pages :" + questionPage.getTotalPages());
        if (user != null && !user.isHasAssignedQuestions()) {
            return "normal/not_assigned";
        }


        if (user.getScore() > 0) {
            int score = user.getScore();
            model.addAttribute("score", score);
            model.addAttribute("name", userName);
            return "normal/exam_completed";
        }

        return "normal/exam_questions";
    }

    @GetMapping("/get-question-ids")
    @ResponseBody
    public List<Long> getQuestionIds(@RequestParam int userId) {
        // Retrieve the assigned questions for the specified user
        List<UserQuestion> userQuestions = userQuestionRepository.findByUserId(userId);

        // Extract the question IDs from the assigned questions
        List<Long> questionIds = userQuestions.stream()
                .map(UserQuestion::getQuestionId)
                .collect(Collectors.toList());
        System.out.println("questionIds : " + questionIds);
        // Return the list of question IDs
        return questionIds;
    }

    @PostMapping("/calculate-score")
    public String calculateScore(Principal principal,
                                 @RequestParam("questionIds[]") List<Long> questionIds,
                                 @RequestParam Map<String, String> selectedOptions,
                                 Model model) {
        String email = principal.getName();
        User user = userRepository.findByEmail(email);


        // Get the name from the user object
        String userName = user.getName();
        int userId = user.getId();

        // Save the start time
        LocalDateTime startTime = user.getStartTime();
        if (startTime == null) {
            startTime = LocalDateTime.now();
            user.setStartTime(startTime);
            userRepository.save(user);
        }

        // Calculate the score and save the answers
        int score = 0;
        List<UserQuestion> userQuestions = new ArrayList<>();

        List<UserQuestion> userQuestion = userQuestionRepository.findByUserId(userId);

        for (Long questionId : questionIds) {
            List<UserQuestion> questions = userQuestionRepository.findByUserIdAndQuestionId(userId, questionId);
            String selectedOption = selectedOptions.get("question-" + questionId);
            Question question = questionRepository.findById(questionId).orElse(null);
            for (UserQuestion userQuestion1 : questions) {
                userQuestion1.setAnswer(selectedOption);

                userQuestion1.setScore(selectedOption != null && selectedOption.equals(question.getCorrectAnswer()) ?
                        question.getTotalMarks() : 0);
                score += userQuestion1.getScore();
                userQuestionRepository.save(userQuestion1);
            }
        }

        // Save the user's answers and update the score
        userQuestionRepository.saveAll(userQuestions);
        user.setScore(score);
        User savedUser = userRepository.save(user);

        // Add the score to the model for display
        model.addAttribute("score", score);
        model.addAttribute("name", userName);
        return "normal/result";
    }

    // Method to complete the exam and set the end time
    public void completeExam(User user) {
        LocalDateTime endTime = LocalDateTime.now();
        user.setEndTime(endTime);
        userRepository.save(user);
    }

    @GetMapping("/notices")
    public String getAllNotices(Model model) {
        List<Notice> notices = noticeRepository.findAll();
        model.addAttribute("notices", notices);
        model.addAttribute("title", "notice page");
        return "normal/notices-page";
    }

}
