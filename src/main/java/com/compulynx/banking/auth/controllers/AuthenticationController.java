package com.compulynx.banking.auth.controllers;

import com.compulynx.banking.auth.models.LogInRequest;
import com.compulynx.banking.auth.models.PinResetRequest;
import com.compulynx.banking.auth.models.SignUpRequest;
import com.compulynx.banking.auth.models.TokenResponse;
import com.compulynx.banking.auth.services.AuthenticationService;
import com.compulynx.banking.auth.services.WebTokenService;
import com.compulynx.banking.utils.ResponseMessage;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/Authentication")
@CrossOrigin(origins = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationService service;

    @Autowired
    private WebTokenService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/Register")
    public ResponseEntity<?> CreateAnAccount(@RequestBody SignUpRequest user) {
        var res = service.addAccount(user);
        if (res != null) {
            if (res.isStatus()) {
                return new ResponseEntity<>(res, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(res, HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/Profile")
    public ResponseEntity<?> UserProfile(@RequestParam String customerId) {
        try {
            var res = service.SearchByCustomerId(customerId);
            if (res != null) {
                return new ResponseEntity<>(res, HttpStatus.OK);
            }
            return new ResponseEntity<>(null, HttpStatus.UNAUTHORIZED);
        } catch (ExpiredJwtException e) {
            return new ResponseEntity<>("JWT Token Expired", HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/ResetCode")
    public ResponseEntity<?> GetPinResetCode(@RequestParam String customerId) {
        var res = service.RequestResetToken(customerId);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/ChangePin")
    public ResponseEntity<?> UpdatePin(@RequestBody PinResetRequest req) {
        var res = service.ResetPin(req);
        if (res != null) {
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/Login")
    public ResponseEntity<?> authenticateAndGetToken(@RequestBody LogInRequest request) {
        var res = new ResponseMessage();

        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getCustomerId(), request.getPin()));
            if (authentication.isAuthenticated()) {
                var token = jwtService.generateToken(request.getCustomerId());
                if (token != null) {
                    token.body = service.SearchByCustomerId(request.getCustomerId());
                }
                res.setMessage("Successful!");
                res.setEntity(token);
                res.setStatus(true);
                return new ResponseEntity<>(res, HttpStatus.OK);
            } else {
                res.setMessage("Authentication Failed!");
                res.setEntity(null);
                res.setStatus(false);
                return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
            }
        } catch (ExpiredJwtException e) {
            res.setMessage("JWT Token Expired!");
            res.setEntity(null);
            res.setStatus(false);
            return new ResponseEntity<>(res, HttpStatus.UNAUTHORIZED);
        } catch (Exception e) {
            res.setMessage("Authentication Failed: " + e.getMessage());
            res.setEntity(null);
            res.setStatus(false);
            return new ResponseEntity<>(res, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
