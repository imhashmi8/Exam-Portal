package com.snort.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "USER")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @NotBlank(message = "Name Field Is required !!")
    @Size(min = 2, max = 20, message = "Character in Name Field Should be Between 2-20 !!")
    private String name;
    @Column(unique = true)
    private String email;
    private String password;
    private String role;
    private boolean enabled;
    private String imageUrl;
    @Column(length = 500)
    private String about;
    private int score;
    private boolean hasAssignedQuestions;
    private boolean hasAttemptExam;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
//    private String status;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "user")//that means if you add user all the field will be selected for user,same in case of delete also,
    private List<Contact> contacts= new ArrayList<>();//it will take contact also
}
