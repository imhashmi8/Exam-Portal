package com.snort.repository;

import com.snort.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {
    @Query("select u from User u where u.email = :email")
    public User getUserByUserName(@Param("email") String email);

    @Query("select u from User u where u.name like %:name%")
    public List<User> findUserByName(String name);

//    public List<User> findByName(String name);

    public User findByEmail(String email);

//    List<User> findAllByUsernameNot(String name);
    @Query("SELECT u FROM User u WHERE u.role <> 'ROLE_ADMIN'")
    Page<User> findAllNonAdminUsers( Pageable pageable);
}
