
package com.compulynx.banking.auth.services;

import com.compulynx.banking.auth.models.*;
import com.compulynx.banking.auth.repositories.AuthenticationRepository;
import com.compulynx.banking.auth.repositories.PasswordResetRepository;
import com.compulynx.banking.core.models.Account;
import com.compulynx.banking.utils.NotificationService;
import com.compulynx.banking.utils.ResponseMessage;
import com.compulynx.banking.utils.UtilityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AuthenticationService implements UserDetailsService {
    @Autowired
    private PasswordResetRepository resetRepository;

    @Autowired
    private AuthenticationRepository repository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private RolesService rolesService;

    @Autowired
    private UtilityService utilityService;

    @Autowired
    private PasswordEncoder encoder;
    @Value("${org.default.roles}")
    private String defaultRoles;

    @Override
    public UserDetails loadUserByUsername(String customerId) throws UsernameNotFoundException {
        Optional<Customer> userDetail = repository.findByCustomerId(customerId);
        // Converting userDetail to UserDetails
        return userDetail.map(CustomerInformation::new)
                .orElseThrow(() -> new UsernameNotFoundException("Account not found " + customerId));
    }

    public Customer SearchByCustomerId(String Id) {
        Optional<Customer> userDetail = repository.findByCustomerId(Id);
        return userDetail.orElse(null);
    }

    public ResponseMessage addAccount(SignUpRequest req) {
        //Generate Pin
        String pin = utilityService.generatePin();

        var user = new Customer();
        var res = new ResponseMessage();
        user.setPin(encoder.encode(pin));
        user.setCustomerId(req.getCustomerId());
        user.setEmailAddress(req.getEmailAddress());
        user.setCustomerName(req.getCustomerName());
        var role = new Role();

        //Assign Role to user
        if (req.getRole().toUpperCase().contains("ADMIN")) {
            role = rolesService.findRoleByName("ROLE_ADMIN");
        } else {
            role = rolesService.findRoleByName("ROLE_USER");
        }

        if (role != null) {
            user.getRoles().add(role);
        }

        if (repository.findByCustomerId(user.getCustomerId()).isEmpty()) {
            //Create Account & set balance to 0.00
            var account = new Account();
            String accountNo = utilityService.generateAccountNumber();
            account.setAccountBalance(new BigDecimal("0.00"));
            account.setAccountName(req.getCustomerName());
            account.setAccountNumber(accountNo);
            var accts = new HashSet<Account>();
            accts.add(account);
            user.setAccounts(accts);
            repository.save(user);
            res.setMessage("Registration Successful!");
            res.setEntity(user);
            res.setStatus(true);

            //Send Email Notification
            String message = "Dear " + user.getCustomerName() + "<br/>Your Credentials are:<br/><h4>Customer Id : " + user.getCustomerId() + "</h4><h4>Pin : " + pin + "</h4>.";
            try {
                notificationService.sendEmailNotification(user.getEmailAddress(), "Account Created Successfully!", message, "no", null);
            } catch (Exception e) {

            }
        } else {
            res.setMessage(user.getCustomerId() + " Already Exists!");
            res.setEntity(null);
            res.setStatus(false);
        }
        return res;
    }

    public List<String> getDefaultRoles() {
        return Arrays.stream(defaultRoles.split("\\s*,\\s*"))
                .collect(Collectors.toList());
    }

    public List<Customer> allCustomers() {
        return repository.findAll();
    }

    public ResponseMessage RequestResetToken(String customerId){
        var res = new ResponseMessage();
        var user = repository.findByCustomerId(customerId);
        if (user.isPresent()) {
            var code = utilityService.generateOTPCode();
            var tk = new PinReset();
            LocalDateTime updatedExpiry;
            var token = resetRepository.findByCustomerId(customerId);
            if (token.isPresent()) {
                tk = token.get();
                updatedExpiry = utilityService.addHours(tk.getExpiryDate(), 5);
                tk.setExpiryDate(updatedExpiry);
                tk.setToken(code);
                tk.setStatus(false);
                resetRepository.save(tk);
            }
            else{
                updatedExpiry = utilityService.addHours(LocalDateTime.now(), 5);
                tk.setExpiryDate(updatedExpiry);
                tk.setStatus(false);
                tk.setCustomerId(customerId);
                tk.setToken(code);
                tk.setCustomer(user.get());
                resetRepository.save(tk);
            }

            //Send Email
            String message = "Dear " + user.get().getCustomerName() + ",<br/>Your pin reset token is <h1>"+code+"</h1> and it is valid for 5 Hours";
            try {
                notificationService.sendEmailNotification(user.get().getEmailAddress(), "Pin Reset Code", message, "no", null);
            } catch (Exception e) {
            }

            res.setMessage("Reset token sent to "+user.get().getEmailAddress());
            res.setEntity(null);
            res.setStatus(true);
        }
        else {
            res.setMessage(customerId+" does not exist!");
            res.setEntity(null);
            res.setStatus(false);
        }
        return res;
    }

    public ResponseMessage ResetPin(PinResetRequest req) {
        var res = new ResponseMessage();
        //Generate Pin
        String pin = utilityService.generatePin();
        var user = repository.findByCustomerId(req.getCustomerId());
        if (user.isPresent()) {
            var token = resetRepository.findByCustomerIdAndToken(req.getCustomerId(),req.getToken());
            if (token.isPresent()) {
                LocalDateTime now = LocalDateTime.now();
                LocalDateTime expiryDate = token.get().getExpiryDate();
                if (now.isAfter(expiryDate)) {
                    res.setMessage("Token Already Expired!");
                    res.setEntity(null);
                    res.setStatus(false);
                } else {
                    var u = user.get();
                    u.setPin(encoder.encode(pin));
                    repository.save(u);
                    res.setMessage("Pin Updated Successfully!");
                    res.setEntity(user);
                    res.setStatus(true);

                    //Send Email Notification
                    String message = "Dear " + u.getCustomerName() + ",<br/>Your Pin has been changed successfully!<br/>New Pin is <h2>"+pin+"</h2>";
                    try {
                        notificationService.sendEmailNotification(u.getEmailAddress(), "Pin Updated Successfully!", message, "no", null);
                    } catch (Exception e) {
                    }
                }
            } else {
                res.setMessage("Invalid Token!");
                res.setEntity(null);
                res.setStatus(false);
            }
        } else {
            res.setMessage(req.getCustomerId() + " Does not exist!");
            res.setEntity(null);
            res.setStatus(false);
        }
        return res;
    }
}

