package com.example.ecommerc.service.impl;
import com.example.ecommerc.dto.Result;
import com.example.ecommerc.dto.UserDto;
import com.example.ecommerc.entity.Cart;
import com.example.ecommerc.entity.Role;
import com.example.ecommerc.entity.User;
import com.example.ecommerc.exception.EmailAlreadyExistException;
import com.example.ecommerc.exception.ResourceNotFoundException;
import com.example.ecommerc.repository.CartRepository;
import com.example.ecommerc.repository.RoleRepository;
import com.example.ecommerc.repository.UserRepository;
import com.example.ecommerc.service.UserService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private CartRepository cartRepository;
    private PasswordEncoder passwordEncoder;
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Result> getUserDetail(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User","id",userId)
        );
        UserDto userDto = modelMapper.map(user,UserDto.class);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", userDto));
    }

    @Override
    public ResponseEntity<Result> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserDto> userDtos = users.stream().map((user)->modelMapper.map(user, UserDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result("SUCCESS", "OK", userDtos));
    }

    @Override
    public ResponseEntity<Result> editInfoUser(Long userId, UserDto userDto) {
        User user = userRepository.findById(userId).orElseThrow(
                ()->new ResourceNotFoundException("User","id",userId)
        );
        if(userRepository.existsUserByUsernameOrEmailAndIdNot(userDto.getUsername(), userDto.getEmail(),userId)){
            throw new EmailAlreadyExistException("Email or Username Already Exists for User");
        }else{
            String encodedPass = passwordEncoder.encode(userDto.getPassword());
            user.setName(userDto.getName());
            user.setPhone(userDto.getPhone());
            user.setAddress(userDto.getAddress());
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getUsername());
            user.setPassword(encodedPass);
            user.setRoles(userDto.getRoles());
            userRepository.save(user);

            UserDto userdto = modelMapper.map(user,UserDto.class);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", userdto));
        }
    }

    @Override
    public ResponseEntity<Result> addUsers(UserDto userDto) {
        if (userRepository.existsUserByUsernameOrEmail(userDto.getUsername(), userDto.getEmail())) {
            throw new EmailAlreadyExistException("Email or Username Already Exists for User");
        }else{
            User user = new User();
            List<Role> rList = new ArrayList<>();
            user.setName(userDto.getName());
            user.setPhone(userDto.getPhone());
            user.setAddress(userDto.getAddress());
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getUsername());
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
            for(Role role:userDto.getRoles()){
                rList.add(roleRepository.findByName(role.getName()).get());
            }
            user.setRoles(rList);
            User saveUser = userRepository.save(user);
            Cart cart = new Cart();
            cart.setUser(saveUser);
            cartRepository.save(cart);

            UserDto userDto1 = modelMapper.map(saveUser, UserDto.class);
            return ResponseEntity.ok(new Result("SUCCESS", "OK", userDto1));
        }
    }

    @Override
    public ResponseEntity<Result> deleteUser(Long usertId) {
        return null;
    }
}
