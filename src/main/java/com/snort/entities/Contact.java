package com.snort.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor

@Setter
@Getter
@Table(name = "CONTACT")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int cId;
    private String name;
    private String secondName;
    private String work;
    @Column(unique = true)
    private String email;
    private String phone;
    private String image;
    @Column(length = 1000)
    private String description;
    @ManyToOne
    @JsonIgnore
    private User user;
}
