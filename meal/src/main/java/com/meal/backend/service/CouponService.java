package com.meal.backend.service;

import com.meal.backend.entity.Coupon;
import com.meal.backend.entity.Employee;
import com.meal.backend.repository.CouponRepository;
import com.meal.backend.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CouponService {

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Transactional
    public Coupon generateCoupon(Long employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        // Check if the employee already has a coupon
        Coupon existingCoupon = employee.getCoupon();
        if (existingCoupon != null) {
            // If a coupon exists, update its ID and expiration time
            existingCoupon.setCouponId(generateUniqueCouponId());
            existingCoupon.setExpirationTime(LocalDateTime.now().plusMinutes(1)); // Update expiration time
            return couponRepository.save(existingCoupon);
        } else {
            // If no coupon exists, create a new one
            Coupon newCoupon = new Coupon();
            newCoupon.setCouponId(generateUniqueCouponId());
            newCoupon.setExpirationTime(LocalDateTime.now().plusMinutes(1)); // Set expiration time
            newCoupon.setEmployee(employee);
            employee.setCoupon(newCoupon);
            employeeRepository.save(employee);
            return couponRepository.save(newCoupon);
        }
    }

    public void deleteCoupon(String couponId) {
        Coupon coupon = couponRepository.findByCouponId(couponId);
        if (coupon != null) {
            couponRepository.delete(coupon);
        }
    }

    private String generateUniqueCouponId() {
        String couponId;
        do {
            couponId = UUID.randomUUID().toString().replaceAll("-", "").substring(0, 16);
        } while (couponRepository.findByCouponId(couponId) != null);
        return couponId;
    }

    @Scheduled(fixedRate = 60000)
    public void deleteExpiredCoupons() {
        LocalDateTime now = LocalDateTime.now();
        List<Coupon> expiredCoupons = couponRepository.findByExpirationTimeBefore(now);
        for (Coupon coupon : expiredCoupons) {
            couponRepository.delete(coupon);
        }
    }

    public ResponseEntity<String> validateCoupon(String couponId) {
        Coupon coupon = couponRepository.findByCouponId(couponId);
        if (coupon != null) {
            if (coupon.getExpirationTime().isBefore(LocalDateTime.now())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Coupon is expired");
            } else {
                return ResponseEntity.ok("Coupon is valid");
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Coupon not found");
        }
    }
}