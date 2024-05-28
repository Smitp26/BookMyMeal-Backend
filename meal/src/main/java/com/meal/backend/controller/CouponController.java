package com.meal.backend.controller;

import com.meal.backend.entity.Coupon;
import com.meal.backend.service.CouponService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin("**")
@RequestMapping("/coupons")
public class CouponController {

    @Autowired
    private CouponService couponService;


    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generateCoupon(@RequestParam Long employeeId) {
        try {
            Coupon coupon = couponService.generateCoupon(employeeId);
            Map<String, String> response = new HashMap<>();
            response.put("couponId", coupon.getCouponId());
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to generate coupon. Please try again later.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateCoupon(@RequestParam String couponId) {
       ResponseEntity<String> responseEntity = couponService.validateCoupon(couponId);
       return responseEntity;
    }
}