package com.meal.backend.service;

import com.meal.backend.repository.BookingRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class CustomUserService {

    private final AuthServiceImpl.UserDetailsServiceImpl userDetailsService; // Inject UserDetailsServiceImpl
    private final BookingRepository bookingRepository;


    public CustomUserService(AuthServiceImpl.UserDetailsServiceImpl userDetailsService, BookingRepository bookingRepository) {
        this.userDetailsService = userDetailsService;
        this.bookingRepository = bookingRepository;
    }

    private Long getUserIdFromToken() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();

        if (principal instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) principal;
            String email = userDetails.getUsername(); // Assuming email is used as username
            UserDetails loadedUserDetails = userDetailsService.loadUserByUsername(email);
            // Assuming YourUserDetailsClass has a method to retrieve employee ID
            if (loadedUserDetails instanceof CustomUserService) {
                return ((CustomUserService) loadedUserDetails).getEmployeeId();
            } else {
                // Handle if loadedUserDetails is not an instance of YourUserDetailsClass
                return null; // or throw an exception
            }
        } else {
            // Handle if principal is not an instance of UserDetails
            return null; // or throw an exception
        }
    }

    private Long getEmployeeId() {
        return this.getUserIdFromToken();
    }
}
