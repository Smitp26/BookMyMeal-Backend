package com.meal.backend.dto;

import com.meal.backend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class SingleBookingRequestDto {

    private Long userId;
    private LocalDate startDate;
    private LocalDate endDate;
    private MealType mealType;
}
