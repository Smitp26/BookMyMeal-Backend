package com.meal.backend.dto;

import com.meal.backend.enums.UserRole;
import lombok.*;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDto {

    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole userRole;

    public EmployeeDto(Long id, String name, String email) {
    }
}
