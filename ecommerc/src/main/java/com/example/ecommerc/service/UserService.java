package com.example.ecommerc.service;


import com.example.ecommerc.dto.Result;
import com.example.ecommerc.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    public ResponseEntity<Result> getUserDetail(Long userId);
    public ResponseEntity<Result> getAllUsers();
    public ResponseEntity<Result>  editInfoUser(Long userId, UserDto userDto);
    public ResponseEntity<Result> addUsers(UserDto userDto);
    public ResponseEntity<Result> deleteUser(Long usertId);
    //public ResponseEntity<Result> getOrderDetail(String username, Long orderId);

}
