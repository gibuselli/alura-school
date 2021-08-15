package br.com.alura.school.enrollment;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;

@Service
public class EnrollmentService {
	
	private final EnrollmentRepository enrollmentRepository;
	private final UserRepository userRepository;
	
	
	public EnrollmentService(EnrollmentRepository enrollmentRepository, UserRepository userRepository) {
		this.enrollmentRepository = enrollmentRepository;
		this.userRepository = userRepository;
	}

	public void validateUserEnrollment(User user, Course course) {
		Enrollment enrollment = enrollmentRepository.findByUser_UsernameAndCourse_Code(user.getUsername(), course.getCode());
		if (enrollment != null) {
			throw new ResponseStatusException(BAD_REQUEST, format("User %s is already enrolled in this course", user.getUsername()));
		}
		
	}

	public List<EnrollmentReportResponse> toReport(List<Enrollment> enrollments, List<User> users) {
		List<EnrollmentReportResponse> reports = new ArrayList<>();		
		Collections.sort(enrollments);
		
		users.forEach(user -> {
			if (user.getEnrollments().size() > 0)
			reports.add(new EnrollmentReportResponse(user.getEmail(), user.getEnrollments().size()));
		});
		
		Collections.sort(reports);
		return reports;
	}

}
