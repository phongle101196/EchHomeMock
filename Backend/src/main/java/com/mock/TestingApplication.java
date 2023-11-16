package com.mock;

import com.mock.config.AppConstants;
import com.mock.entity.Role;
import com.mock.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@SpringBootApplication
public class TestingApplication implements CommandLineRunner {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(TestingApplication.class, args);
	}
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println(this.passwordEncoder.encode("123456"));
		try {

			Role role1 = new Role();
			role1.setId(AppConstants.ROLE_ADMIN);
			role1.setName("ROLE_ADMIN");

			Role role2 = new Role();
			role2.setId(AppConstants.ROLE_POSTER);
			role2.setName("ROLE_POSTER");

			Role role3 = new Role();
			role3.setId(AppConstants.ROLE_USER);
			role3.setName("ROLE_USER");
			List<Role> roles = List.of(role1,role2,role3);

			List<Role> result = this.roleRepository.saveAll(roles);
			result.forEach(r ->{
//				System.out.println(r.getName());
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}

}
