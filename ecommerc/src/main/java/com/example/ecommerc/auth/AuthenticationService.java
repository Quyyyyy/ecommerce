package com.example.ecommerc.auth;

import com.example.ecommerc.entity.Cart;
import com.example.ecommerc.entity.Role;
import com.example.ecommerc.entity.User;
import com.example.ecommerc.repository.CartRepository;
import com.example.ecommerc.repository.RoleRepository;
import com.example.ecommerc.repository.UserRepository;
import com.example.ecommerc.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CartRepository cartRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<AuthenticationResponse> register(RegisterRequest request) {
        try{
            if (userRepository.existsUserByUsernameOrEmail(request.getUsername(), request.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(AuthenticationResponse.builder().message("Username or Email is already used").token(null).build());
            }
            Role roles = roleRepository.findByName("ROLE_USER").get();
            Cart cart = new Cart();
            User user = User.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .name(request.getName())
                    .email(request.getEmail())
                    .roles(Collections.singletonList(roles))
                    .build();
            User saveUser = userRepository.save(user);
            cart.setUser(saveUser);
            cartRepository.save(cart);

            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder().message("Register successfully").token(jwtToken).build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<AuthenticationResponse> authenticate(AuthenticationRequest request) {
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()));
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow(null);
            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder().message("Login success").token(jwtToken).build());
        } catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthenticationResponse.builder().message("Invalid username or password").build());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    public ResponseEntity<AuthenticationResponse> changePassword(ChangePasswordRequest request) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));
            User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            String jwtToken = jwtService.generateToken(user);
            return ResponseEntity.ok(AuthenticationResponse.builder().message("Change Password successfully").token(jwtToken).build());
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthenticationResponse.builder().message("Invalid username or password").build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}
