package com.example.security.security.account;

import org.springframework.security.core.GrantedAuthority;

public enum AccountRole implements GrantedAuthority {
	ADMIN,
	USER;

	@Override
	public String getAuthority() {
		return "ROLE_" + name();
	}
}
