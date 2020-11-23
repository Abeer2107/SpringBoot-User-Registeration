/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tests.AuthSystem.repos;

/**
 *
 * @author abeer
 */
import com.tests.AuthSystem.entities.User;
import java.util.List;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("from User where email=?1")
    public List<User> findAllByEmail(String email);

    @Query("from User where email=?1")
    public User findByEmail(String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.firstName = :first_name, u.lastName = :last_name WHERE u.email = :email")
    public void editUserProfile(@Param("first_name") String fname, @Param("last_name") String lname, @Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.password = :password WHERE u.email = :email")
    public void editUserPass(@Param("password") String password, @Param("email") String email);

}
