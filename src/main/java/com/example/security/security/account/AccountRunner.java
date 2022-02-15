package com.example.security.security.account;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AccountRunner implements ApplicationRunner {
	private final AccountRepository accountRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Account adminAccount = Account.builder()
				.username("admin")
				.password("admin")
				.role(AccountRole.ADMIN)
				.build();

		adminAccount.encodePassword(passwordEncoder);

		accountRepository.save(adminAccount);
		
		Account normalAccount = Account.builder()
			.username("user")
			.password("user")
			.role(AccountRole.USER)
			.build();
		
		normalAccount.encodePassword(passwordEncoder);
		
		accountRepository.save(normalAccount);
	}
}