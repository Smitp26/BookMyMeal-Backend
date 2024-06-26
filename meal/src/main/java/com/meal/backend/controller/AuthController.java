package com.meal.backend.controller;

import com.meal.backend.dto.AuthenticationRequest;
import com.meal.backend.dto.AuthenticationResponse;
import com.meal.backend.dto.EmployeeDto;
import com.meal.backend.dto.SignupRequest;
import com.meal.backend.entity.Employee;
import com.meal.backend.repository.EmployeeRepository;
import com.meal.backend.service.AuthService;
import com.meal.backend.service.AuthServiceImpl;
import com.meal.backend.service.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

@CrossOrigin("**")
@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final AuthServiceImpl.UserDetailsServiceImpl userDetailsService;
    private final JwtUtil jwtUtil;
    private final EmployeeRepository employeeRepository;

    public AuthController(AuthService authService, AuthenticationManager authenticationManager, AuthServiceImpl.UserDetailsServiceImpl userDetailsService, JwtUtil jwtUtil, EmployeeRepository employeeRepository) {
        this.authService = authService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.employeeRepository = employeeRepository;
    }

    @GetMapping("/home")
    public ResponseEntity<String> home(){
        return ResponseEntity.ok("Welcome to home page!");
    }


    @PostMapping("/signup")
    public ResponseEntity<?> signupuser(@RequestBody SignupRequest signupRequest ){
        if(employeeRepository.findFirstByEmail(signupRequest.getEmail()).isPresent()){
            return ResponseEntity.status(409).body("User Already Exists");
        }
        try{
            EmployeeDto createdUserDto = authService.createUser(signupRequest);
            return new ResponseEntity<>(createdUserDto,HttpStatus.CREATED);
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest, HttpServletResponse response) throws IOException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
            }
        catch(Exception e){
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\": \"Wrong username or password\"}");
            response.getWriter().flush();
            return null;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());
        Optional<Employee> optionalUser = employeeRepository.findFirstByEmail(userDetails.getUsername());
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if(optionalUser.isPresent()){
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setName(optionalUser.get().getName());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
            authenticationResponse.setUserId((optionalUser.get().getId()));
        }
        return authenticationResponse;
    }
}
