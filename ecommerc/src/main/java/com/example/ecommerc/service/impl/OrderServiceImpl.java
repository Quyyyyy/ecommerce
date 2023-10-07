package com.example.ecommerc.service.impl;

import com.example.ecommerc.dto.*;
import com.example.ecommerc.entity.*;
import com.example.ecommerc.exception.APIException;
import com.example.ecommerc.exception.ResourceNotFoundException;
import com.example.ecommerc.repository.*;
import com.example.ecommerc.service.EmailService;
import com.example.ecommerc.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private CartItemRepository cartItemRepository;
    private CartRepository cartRepository;
    private ProductRepository productRepository;
    private CouponRepository couponRepository;
    private PaymentRepository paymentRepository;
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private EmailService emailService;


    @Override
    public ResponseEntity<Result> getAllUserOrder(String username, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Order> orders = orderRepository.findOrderByUser(userRepository.findByUsername(username).get(),pageRequest);
        List<OrderDto> orderDtoList = orders.stream().map((order)->modelMapper.map(order,OrderDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result("SUCCESS","OK",orderDtoList));
    }

    @Override
    public ResponseEntity<Result> getAllOrder(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page,size);
        Page<Order> orders = orderRepository.findAll(pageRequest);
        List<OrderDto> orderDtoList = orders.stream().map((order)->modelMapper.map(order,OrderDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result("SUCCESS","OK",orderDtoList));
    }

    @Override
    public ResponseEntity<Result> orderProduct(String username,OrderDto orderDto) {
        List<OrderItem> orderItems = new ArrayList<>();
        List<CartItem> cartItems = new ArrayList<>();
        Double total_price = (double) 0;
        User user = userRepository.findByUsername(username).get();
        Cart cart = cartRepository.findCartByUser(user).get();
        List<CartItem> cartItemList = cartItemRepository.findCartItemByCart(cart);

        Order order = new Order();
        order.setUser(user);
        order.setStatusShipping("Đã đặt");
        order.setEmail(orderDto.getEmail());
        order.setName(orderDto.getName());
        order.setAddress(orderDto.getAddress());
        order.setPhone(orderDto.getPhone());

        PaymentDto paymentDto = orderDto.getPaymentDto();
        Payment payment = modelMapper.map(paymentDto, Payment.class);
        Payment savePay = paymentRepository.save(payment);

        order.setPayment(savePay);
        order.setOrderDate(LocalDate.now());
        for(CartItem cartItem: cartItemList){
            Product product = cartItem.getProduct();
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setTotalPrice(cartItem.getQuantity()*product.getPrice());
            if(product.getQuantity() > cartItem.getQuantity()){
                product.setQuantity(product.getQuantity() - cartItem.getQuantity());
                productRepository.save(product);
            }else if (product.getQuantity() == cartItem.getQuantity()) {
                product.setQuantity(0);
                product.setStatus(false);
                productRepository.save(product);
            } else {
                throw  new APIException("NOT ENOUGH PRODUCT");
            }
            total_price += orderItem.getTotalPrice();
            orderItems.add(orderItem);
            cartItems.add(cartItem);
        }

        Coupon coupon = couponRepository.findById(orderDto.getCouponDto().getId()).orElse(null);
        if(coupon.getMaxUsage()>1){
            coupon.setMaxUsage(coupon.getMaxUsage()-1);
        } else if(coupon.getMaxUsage() == 1){
            coupon.setMaxUsage(coupon.getMaxUsage()-1);
            coupon.setStatus(false);
        } else{
            throw new APIException("NOT ENOUGH COUPON");
        }

        if (total_price >= coupon.getConditionDiscount()){
            switch (coupon.getType()){
                case PERCENT_ON_ORDER:
                    int discountPercent = Double.valueOf(coupon.getDiscount()).intValue();
                    total_price = (total_price * (100 - discountPercent)) / 100;
                    break;
                case DIRECT_ON_ORDER:
                    total_price = total_price - coupon.getDiscount();
                    break;
            }
        } else{
            throw new APIException("NOT ENOUGH COUPON");
        }
        order.setTotalPrice(total_price);
        Coupon coupon1 = couponRepository.save(coupon);

        Order order1 = orderRepository.save(order);
        for (OrderItem orderItem : orderItems) {
            orderItem.setOrder(order1);
        }
        orderItemRepository.saveAll(orderItems);
        cartItemRepository.deleteAll(cartItems);

        OrderDto orderDto1 = modelMapper.map(order1,OrderDto.class);
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        for(OrderItem orderItem:orderItems){
            OrderItemDto orderItemDto = modelMapper.map(orderItem,OrderItemDto.class);
            orderItemDto.setProduct(modelMapper.map(orderItem.getProduct(), ProductDto.class));
            orderItemDtoList.add(orderItemDto);
        }

        orderDto1.setPaymentDto(modelMapper.map(savePay,PaymentDto.class));
        orderDto1.setCouponDto(modelMapper.map(coupon1,CouponDto.class));
        orderDto1.setOrderItemDtos(orderItemDtoList);

        emailService.sendMail(order1, user);

        return  ResponseEntity.ok(new Result("ORDER SUCCESS", "OK", orderDto1));
    }

    @Override
    public ResponseEntity<Result> getOrderDetail(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                ()->new ResourceNotFoundException("Order","id",orderId)
        );
        OrderDto orderDto = modelMapper.map(order,OrderDto.class);
        List<OrderItem> orderItemList = orderItemRepository.findOrderItemByOrder(order);
        List<OrderItemDto> orderItemDtoList = new ArrayList<>();
        for(OrderItem orderItem:orderItemList){
            OrderItemDto itemDto = modelMapper.map(orderItem,OrderItemDto.class);
            itemDto.setProduct(modelMapper.map(orderItem.getProduct(), ProductDto.class));
            orderItemDtoList.add(itemDto);
        }
        orderDto.setOrderItemDtos(orderItemDtoList);
        return ResponseEntity.ok(new Result("SUCCESS","OK",orderDto));
    }
}
