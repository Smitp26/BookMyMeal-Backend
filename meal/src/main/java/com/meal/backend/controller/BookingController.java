package com.meal.backend.controller;
import com.meal.backend.dto.BookingRequestDto;
import com.meal.backend.dto.QuickBookingRequestDto;
import com.meal.backend.dto.SingleBookingRequestDto;
import com.meal.backend.entity.Booking;
import com.meal.backend.repository.BookingRepository;
import com.meal.backend.service.BookingService;
import com.meal.backend.service.CustomUserService;
import com.meal.backend.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;
import java.util.Collections;


@RestController
@RequestMapping("/api/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired

    private BookingRepository bookingRepository ;

    @Autowired

    private NotificationService notificationService;

    @Autowired
    private CustomUserService customUserService;

    public BookingController(CustomUserService customUserService, BookingRepository bookingRepository) {
        this.customUserService = customUserService;
        this.bookingRepository = bookingRepository;
    }
    @PostMapping("/quickBook")
    public ResponseEntity<?> quickBookMeal(@RequestBody QuickBookingRequestDto request) {
        try {
            Booking booking = bookingService.quickBookMeal(request);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/bulk")
    public ResponseEntity<?> bookMeals(@RequestBody BookingRequestDto request) {
        try {
            List<Booking> bookings = (List<Booking>) bookingService.bookMeals(request);
            return ResponseEntity.ok(bookings);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/single")
    public ResponseEntity<?> bookSingleMeal(@RequestBody SingleBookingRequestDto request) {
        try {
            Booking booking = bookingService.bookSingleMeal(request);
            return ResponseEntity.ok(booking);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/cancel")
    public ResponseEntity<Map<String, String>> cancelBooking(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate cancellationDate) {
        try {
            return bookingService.cancelBooking(cancellationDate);
        } catch (Exception e) {
            // Handle any other exceptions
            e.printStackTrace(); // Print the stack trace for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("message", "An error occurred: " + e.getMessage()));
        }
    }

    @GetMapping("/dates/{userId}")
    public List<LocalDate> getBookingDatesForUser(@PathVariable Long userId) {
        return bookingService.getBookingDatesForUser(userId);
    }

}