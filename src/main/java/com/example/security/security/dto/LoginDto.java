package com.example.security.security.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter
@ToString(exclude = "password")
public class LoginDto {
	@NotNull
	@Size(min = 1, max = 50)
	private String username;

	@NotNull
	@Size(min = 4, max = 100)
	private String password;
}
