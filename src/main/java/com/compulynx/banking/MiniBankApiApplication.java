package com.compulynx.banking;

import com.compulynx.banking.auth.models.Role;
import com.compulynx.banking.auth.models.SignUpRequest;
import com.compulynx.banking.auth.services.AuthenticationService;
import com.compulynx.banking.auth.services.RolesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Component;

import java.util.List;

@SpringBootApplication
public class MiniBankApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(MiniBankApiApplication.class, args);
	}

	@Component
	public static class AdminData implements CommandLineRunner {
		@Value("${org.senior.admin.Id}")
		private String adminId;
		@Value("${org.senior.admin.email}")
		private String adminEmail;

		@Value("${org.senior.admin.name}")
		private String adminName;

		@Autowired
		private AuthenticationService authenticationService;

		@Autowired
		private RolesService rolesService;

		void CreateDefaultRoles() {
			if (rolesService.allRoles().isEmpty()) {
				List<String> roles = authenticationService.getDefaultRoles();
				for (var i : roles) {
					var r = new Role();
					r.setName(i);
					rolesService.addRole(r);
				}
			}
		}

		@Override
		public void run(String... args) throws Exception {
			if(authenticationService.allCustomers().isEmpty()){
				if(rolesService.allRoles().isEmpty()) {
					CreateDefaultRoles();
				}
				var req = new SignUpRequest();
				req.setEmailAddress(adminEmail);
				req.setCustomerId(adminId);
				req.setCustomerName(adminName);
				req.setRole("Admin");
				authenticationService.addAccount(req);
			}
		}
	}
}
