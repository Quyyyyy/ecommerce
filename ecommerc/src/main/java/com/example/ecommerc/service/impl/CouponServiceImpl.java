package com.example.ecommerc.service.impl;

import com.example.ecommerc.dto.CouponDto;
import com.example.ecommerc.dto.Result;
import com.example.ecommerc.entity.Coupon;
import com.example.ecommerc.exception.APIException;
import com.example.ecommerc.exception.ResourceNotFoundException;
import com.example.ecommerc.repository.CouponRepository;
import com.example.ecommerc.service.CouponService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CouponServiceImpl implements CouponService {
    private CouponRepository couponRepository;
    private ModelMapper modelMapper;

    @Override
    public ResponseEntity<Result> getAllCoupon(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Coupon> cous = couponRepository.findAll(pageRequest);
        List<CouponDto> couponDtos = cous.stream().map((cou)->modelMapper.map(cou,CouponDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(new Result("SUCCESS","OK",couponDtos));
    }

    @Override
    public ResponseEntity<Result> addCoupon(CouponDto couponDto) {
        Boolean check = couponRepository.existsCouponByCode(couponDto.getCode());
        if(!check){
            Coupon coupon = new Coupon();
            coupon.setCode(couponDto.getCode());
            coupon.setMaxUsage(couponDto.getMaxUsage());
            coupon.setDiscount(couponDto.getDiscount());
            coupon.setType(couponDto.getType());
            coupon.setConditionDiscount(couponDto.getConditionDiscount());
            if(couponDto.getExpirationDate().compareTo(new Date())<0){
                throw new APIException("CONFLICT DATE");
            } else{
                coupon.setExpirationDate(couponDto.getExpirationDate());
            }
            Coupon saveCoupon = couponRepository.save(coupon);
            CouponDto couponDto1 = modelMapper.map(saveCoupon,CouponDto.class);
            return ResponseEntity.ok(new Result("SUCCESS","OK",couponDto1));
        }else{
            throw new APIException("EXIST COUPON");
        }
    }

    @Override
    public ResponseEntity<Result> updateCoupon(CouponDto couponDto, Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                ()->new ResourceNotFoundException("Coupon","id",couponId)
        );
        if((couponDto.getCode()!=null) && (!couponDto.getCode().equals(coupon.getCode()))){
            if(couponRepository.existsCouponByCode(couponDto.getCode())){
                throw new APIException("EXIST COUPON");
            } else{
                coupon.setCode(couponDto.getCode());
            }
        }
        if(couponDto.getDiscount() != null){
            coupon.setDiscount(couponDto.getDiscount());
        }
        if (couponDto.getExpirationDate() != null){
            if(couponDto.getExpirationDate().compareTo(new Date()) < 0){
                throw new APIException("CONFLICT DATE");
            }
            else{
                coupon.setExpirationDate(couponDto.getExpirationDate());
            }
        }
        if(couponDto.getMaxUsage() != 0){
            coupon.setMaxUsage(couponDto.getMaxUsage());
        }
        if(couponDto.getType() != null){
            coupon.setType(couponDto.getType());
        }
        if(couponDto.getConditionDiscount()!= null){
            coupon.setConditionDiscount(couponDto.getConditionDiscount());
        }
        Coupon coupon1 = couponRepository.save(coupon);
        CouponDto couponDto1 = modelMapper.map(coupon1,CouponDto.class);
        return ResponseEntity.ok(new Result("SUCCESS","OK",couponDto1));
    }

    @Override
    public ResponseEntity<Result> deleteCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                ()->new ResourceNotFoundException("Coupon","id",couponId)
        );
        coupon.setStatus(false);
        Coupon coupon1 = couponRepository.save(coupon);
        CouponDto couponDto1 = modelMapper.map(coupon1,CouponDto.class);
        return ResponseEntity.ok(new Result("SUCCESS","OK",couponDto1));
    }

    @Override
    public ResponseEntity<Result> activeCoupon(Long couponId) {
        Coupon coupon = couponRepository.findById(couponId).orElseThrow(
                ()->new ResourceNotFoundException("Coupon","id",couponId)
        );
        coupon.setStatus(true);
        Coupon coupon1 = couponRepository.save(coupon);
        CouponDto couponDto1 = modelMapper.map(coupon1,CouponDto.class);
        return ResponseEntity.ok(new Result("SUCCESS","OK",couponDto1));
    }
}
