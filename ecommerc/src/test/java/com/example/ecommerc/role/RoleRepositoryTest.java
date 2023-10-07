package com.example.ecommerc.role;

import com.example.ecommerc.entity.Role;
import com.example.ecommerc.entity.User;
import com.example.ecommerc.repository.RoleRepository;
import com.example.ecommerc.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    void saveMethod(){
        // create product
        Role role = new Role();
        Role role1 = new Role();
        role.setName("ADMIN");
        role1.setName("USER");
        roleRepository.save(role);
        roleRepository.save(role1);
    }

    @Test
    void checkMethod(){
        User user = userRepository.findById(1L).get();


        System.out.println(userRepository.existsUserByUsernameOrEmailAndIdNot(user.getUsername(),user.getEmail(),user.getId()));

    }
}
