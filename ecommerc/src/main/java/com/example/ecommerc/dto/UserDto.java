package com.example.ecommerc.dto;

import com.example.ecommerc.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    private String phone;
    private String address;
    private String email;
    private String username;
    private String password;
    private List<Role> roles = new ArrayList<>();
    private CartDto cart;
}
