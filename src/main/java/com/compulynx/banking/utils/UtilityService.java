package com.compulynx.banking.utils;

import com.compulynx.banking.auth.models.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

/*
 *I have used this class to store all methods that I can use across the application
 */
@Component
public class UtilityService {

    Random random = new Random();

    public List<GrantedAuthority> mapUserAuthorities(Collection<Role> roles){
        List<GrantedAuthority> authorities = new ArrayList<>();
        for(Role role : roles){
                authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    public LocalDateTime addHours(LocalDateTime dateTime, long hoursToAdd) {
        return dateTime.plusHours(hoursToAdd);
    }

    public String generateAccountNumber(){
        int minAcctNo = 100000000;
        int maxAcctNo = 999999999;
        int randomAccount = random.nextInt(maxAcctNo - minAcctNo + 1) + minAcctNo;
        return String.format("%09d", randomAccount);
    }

    public String generatePin(){
        int min = 1000;
        int max = 9999;
        int randomPin = random.nextInt(max - min + 1) + min;
        return String.format("%04d", randomPin);
    }

    public String generateOTPCode(){
        int min = 100000;
        int max = 999999;
        int randomNumber = random.nextInt(max - min + 1) + min;
        return String.format("%04d", randomNumber);
    }

    public String generateTransactionCode(int length) {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return sb.toString();
    }
}
