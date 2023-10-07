package com.example.ecommerc.repository;

import com.example.ecommerc.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    //Optional<User> findByUsernameAndStatusIsNotFalse(String username);
    Optional<User> findByUsername(String username);
    Boolean existsUserByUsernameOrEmail(String username,String email);
    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE (u.username = :username OR u.email = :email) AND u.id <> :id")
    Boolean existsUserByUsernameOrEmailAndIdNot(@Param("username") String username, @Param("email") String email, @Param("id") Long id);

}
