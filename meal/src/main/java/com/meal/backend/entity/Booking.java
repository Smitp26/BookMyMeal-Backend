package com.meal.backend.entity;

import com.meal.backend.enums.MealType;
import com.meal.backend.enums.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDate;

@Entity
@Data
@Table(name = "booking")
@AllArgsConstructor
@NoArgsConstructor
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Employee employee;

    private LocalDate startDate;

    private LocalDate endDate;

    private MealType mealType;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL)
    private Coupon coupon;
}