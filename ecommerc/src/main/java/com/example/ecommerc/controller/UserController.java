package com.example.ecommerc.controller;

import com.example.ecommerc.dto.CartItemDto;
import com.example.ecommerc.dto.OrderDto;
import com.example.ecommerc.dto.Result;
import com.example.ecommerc.dto.UserDto;
import com.example.ecommerc.security.JwtService;
import com.example.ecommerc.service.CartService;
import com.example.ecommerc.service.OrderService;
import com.example.ecommerc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {

    private UserService userService;
    private JwtService jwtService;
    private CartService cartService;
    private OrderService orderService;

    @GetMapping("/user/{id}")
    public ResponseEntity<Result> getUserById(@PathVariable("id") Long userId){
        return userService.getUserDetail(userId);
    }

    @GetMapping("/user")
    public ResponseEntity<Result> getUserById(){
        return userService.getAllUsers();
    }

    @PostMapping("/user/{id}")
    public ResponseEntity<Result> editInfoUser(@PathVariable("id") Long userId,@RequestBody UserDto userDto){
        return userService.editInfoUser(userId,userDto);
    }

    @PostMapping("/user/create")
    public ResponseEntity<Result> editInfoUser(@RequestBody UserDto userDto){
        return userService.addUsers(userDto);
    }




    //cart
    @GetMapping("/cart/product-list")
    public ResponseEntity<Result> getCart(HttpServletRequest httpServletRequest){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        return cartService.getCart(username);
    }

    @PostMapping("/add-to-cart/{productId}")
    public ResponseEntity<Result> addToCart(HttpServletRequest httpServletRequest,@RequestParam Integer quantity,
                                            @PathVariable("productId") Long productId){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        return cartService.addToCart(productId,quantity, username);
    }

    @PostMapping("/cart/update-quantity/{cartItemId}")
    public ResponseEntity<Result> updateQuantityProduct(HttpServletRequest httpServletRequest,
                                                        @PathVariable("cartItemId") Long cartItemId,
                                                        @RequestBody CartItemDto cartItem){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        return cartService.updateQuantityProduct(username,cartItem,cartItemId);
    }

    @PostMapping("/cart/remove-product/{cartItemId}")
    public ResponseEntity<Result> removeItemInCart(HttpServletRequest httpServletRequest,
                                                   @PathVariable("cartItemId") Long cartItemId){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        return cartService.removeItemInCart(username,cartItemId);
    }



    @PostMapping("/cart/order")
    public ResponseEntity<Result> orderProduct(HttpServletRequest httpServletRequest,
                                               @RequestBody OrderDto orderDto){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        return orderService.orderProduct(username, orderDto);
    }

    @GetMapping("/order/detail/{orderId}")
    public ResponseEntity<Result> getOrderDetail(HttpServletRequest httpServletRequest,
                                                 @PathVariable("orderId") Long orderId){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        return orderService.getOrderDetail(orderId);
    }

    @GetMapping("/user/order/all")
    public ResponseEntity<Result> getAllOrder(HttpServletRequest httpServletRequest,
                                              @RequestParam(defaultValue = "0") int page,
                                              @RequestParam(defaultValue = "8") int size){
        String token = httpServletRequest.getHeader("Authorization").substring(7);
        String username = jwtService.extractUsername(token);
        return orderService.getAllUserOrder(username, page, size);
    }
}
