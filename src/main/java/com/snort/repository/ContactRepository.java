package com.snort.repository;

import com.snort.entities.Contact;
import com.snort.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ContactRepository extends JpaRepository<Contact,Integer> {

    @Query("from Contact as c where c.user.id =:userId")
    public List<Contact>  findContactByUser(@Param("userId") int userId);
    public  List<Contact> findByName(String name);
    public List<Contact> findByEmail(String email);
    public List<Contact> findByPhone(String phone);

    /*for dynamic search*/
    public List<Contact> findByNameContainingAndUser(String name, User user);

}
