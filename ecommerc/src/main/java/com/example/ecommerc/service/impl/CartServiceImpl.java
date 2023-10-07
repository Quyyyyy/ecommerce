package com.example.ecommerc.service.impl;

import com.example.ecommerc.dto.CartDto;
import com.example.ecommerc.dto.CartItemDto;
import com.example.ecommerc.dto.Result;
import com.example.ecommerc.entity.Cart;
import com.example.ecommerc.entity.CartItem;
import com.example.ecommerc.entity.Product;
import com.example.ecommerc.entity.User;
import com.example.ecommerc.exception.APIException;
import com.example.ecommerc.exception.ResourceNotFoundException;
import com.example.ecommerc.repository.CartItemRepository;
import com.example.ecommerc.repository.CartRepository;
import com.example.ecommerc.repository.ProductRepository;
import com.example.ecommerc.repository.UserRepository;
import com.example.ecommerc.service.CartService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {
    private CartRepository cartRepository;
    private CartItemRepository cartItemRepository;
    private UserRepository userRepository;
    private ProductRepository productRepository;
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Result> addToCart(Long productId,Integer quantity, String username) {
        Product product = productRepository.findById(productId).orElseThrow(
                ()->new ResourceNotFoundException("Product","id",productId)
        );
        User user = userRepository.findByUsername(username).orElse(null);
        Cart cart = cartRepository.findCartByUser(user).get();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart = cartRepository.save(cart);
        }
        List<CartItem> cartItems = cartItemRepository.findCartItemByCart(cart);
        Boolean check = false;
        for(CartItem cartItem:cartItems){
            if(cartItem.getProduct().equals(product)){
                cartItem.setQuantity(cartItem.getQuantity()+quantity);
                check = true;
                break;
            }
        }
        if(!check){
            CartItem cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setQuantity(quantity);
            cartItem.setProduct(product);
            cartItems.add(cartItem);
        }
        cartItemRepository.saveAll(cartItems);
        CartDto cartDto = modelMapper.map(cart,CartDto.class);
        List<CartItemDto> cartItemDtos = cartItems.stream().map((cartItem)->modelMapper.map(cartItem,CartItemDto.class))
                .collect(Collectors.toList());
        cartDto.setCartItemDtos(cartItemDtos);
        return ResponseEntity.ok(new Result("SUCCESS","OK",cartDto));
    }

    @Override
    public ResponseEntity<Result> getCart(String username) {
        Cart cart = cartRepository.findCartByUser(userRepository.findByUsername(username).get()).get();
        if (cart == null) {
            throw new APIException("NOT FOUND CART");
        }
        List<CartItem> cartItems = cartItemRepository.findCartItemByCart(cart);
        List<CartItemDto> cartItemDtos = cartItems.stream().map((cartItem)->modelMapper.map(cartItem,CartItemDto.class))
                .collect(Collectors.toList());
        //cartDto.setCartItemDtos(cartItemDtos);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", cartItemDtos));
    }

    @Override
    public ResponseEntity<Result> removeItemInCart(String username, Long itemCartId) {
        Cart cart = cartRepository.findCartByUser(userRepository.findByUsername(username).get()).get();
        if (cart == null) {
            throw new APIException("NOT FOUND CART");
        }
        CartItem cartItem = cartItemRepository.findById(itemCartId).orElseThrow(null);
        if (cartItem == null) {
            throw new APIException("NOT FOUND CARTITEM");
        }
        cartItemRepository.deleteById(itemCartId);
        List<CartItem> cartItemList = cartItemRepository.findCartItemByCart(cart);
        return ResponseEntity.ok(new Result("SUCCESS", "OK", cartItemList));

    }

    @Override
    public ResponseEntity<Result> updateQuantityProduct(String username, CartItemDto cartItemDto, Long cartItemId) {
        Cart cart = cartRepository.findCartByUser(userRepository.findByUsername(username).get()).get();
        if (cart == null) {
            throw new APIException("NOT FOUND CART");
        }
        List<CartItem> cartItemList = cartItemRepository.findCartItemByCart(cart);
        for(CartItem cartItem:cartItemList){
            if(cartItem.getId().equals(cartItemId)){
                if(cartItemDto.getQuantity().intValue()<=0){
                    cartItemList.remove(cartItem);
                    cartItemRepository.delete(cartItem);
                    break;
                }else{
                    cartItem.setQuantity(cartItemDto.getQuantity());
                    cartItemRepository.save(cartItem);
                    break;
                }
            }
        }
        CartDto cartDto = modelMapper.map(cart,CartDto.class);
        List<CartItemDto> cartItemDtos = cartItemList.stream().map((cartItem)->modelMapper.map(cartItem,CartItemDto.class))
                .collect(Collectors.toList());
        cartDto.setCartItemDtos(cartItemDtos);
        return ResponseEntity.ok(new Result("SUCCESS","OK",cartDto));
    }
}
