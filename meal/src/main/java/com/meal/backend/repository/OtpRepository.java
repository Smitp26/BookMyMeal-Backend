package com.meal.backend.repository;

import com.meal.backend.entity.Employee;
import com.meal.backend.entity.Otp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OtpRepository extends JpaRepository<Otp,Long> {
    Otp findByOtp(String otp);

    Otp findByUser(Employee user);
}
