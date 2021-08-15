package br.com.alura.school.enrollment;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

@Service
public class EnrollmentService {
	
	private final EnrollmentRepository enrollmentRepository;
	
	public EnrollmentService(EnrollmentRepository enrollmentRepository) {
		this.enrollmentRepository = enrollmentRepository;		
	}

	public void validateUserEnrollment(User user, Course course) {
		Enrollment enrollment = enrollmentRepository.findByUser_UsernameAndCourse_Code(user.getUsername(), course.getCode());
		if (enrollment != null) {
			throw new ResponseStatusException(BAD_REQUEST, format("User %s is already enrolled in this course", user.getUsername()));
		}
		
	}
	
	

}
