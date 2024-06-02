package com.meal.backend.dto;

import com.meal.backend.enums.MealType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class QuickBookingRequestDto {

    private  Long userId;
    private  MealType mealType;

}
