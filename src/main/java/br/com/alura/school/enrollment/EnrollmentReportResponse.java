package br.com.alura.school.enrollment;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnrollmentReportResponse implements Comparable<EnrollmentReportResponse> {

	@JsonProperty
	private String email;
	
	@JsonProperty(value = "quantidade_matriculas")
	private Integer enrollments;

	public EnrollmentReportResponse(String email, Integer enrollments) {
		this.email = email;
		this.enrollments = enrollments;
	}

	public String getEmail() {
		return email;
	}

	public Integer getEnrollments() {
		return enrollments;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setEnrollments(int enrollments) {
		this.enrollments = enrollments;
	}

	@Override
	public int compareTo(EnrollmentReportResponse o) {
		return o.getEnrollments().compareTo(this.getEnrollments());
	}
	
	
	
	
		
	
}
