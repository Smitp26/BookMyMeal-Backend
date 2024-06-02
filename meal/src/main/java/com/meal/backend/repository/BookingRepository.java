package com.meal.backend.repository;

import com.meal.backend.entity.Booking;
import com.meal.backend.entity.Employee;
import com.meal.backend.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
@EnableJpaRepositories
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Object> findByEmployeeIdAndStartDateBetween(Long id, LocalDate startDate, LocalDate endDate);

    List<Booking> findByEmployeeIdAndStartDate(Long userId, LocalDate startDate);

    List<Booking> findByEmployee(Employee loggedInUser);

    List<Booking> findByStartDateIn(List<LocalDate> bookingDates);

    List<Booking> findByStartDate(LocalDate cancellationDate);

    List<Booking> findByEmployeeAndStatus(Employee user, Status status);
}
