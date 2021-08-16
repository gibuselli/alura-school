package br.com.alura.school.enrollment;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class NewEnrollmentRequest {

	@NotBlank
	@JsonProperty
	private final String username;

	@JsonCreator
	public NewEnrollmentRequest(@NotBlank @JsonProperty("username") String username) {
		this.username = username;
	}

	public String getUsername() {
		return username;
	}	

	
}
