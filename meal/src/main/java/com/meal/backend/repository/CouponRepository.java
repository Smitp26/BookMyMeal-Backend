package com.meal.backend.repository;

import com.meal.backend.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CouponRepository extends JpaRepository<Coupon, Long> {

    Coupon findByCouponId(String couponId);

    List<Coupon> findByExpirationTimeBefore(LocalDateTime expirationTime);
}
