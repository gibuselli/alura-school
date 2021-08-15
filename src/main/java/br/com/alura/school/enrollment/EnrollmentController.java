package br.com.alura.school.enrollment;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;

@RestController
class EnrollmentController {
	
	private final EnrollmentRepository enrollmentRepository;
	private final EnrollmentService enrollmentService;
	private final UserRepository userRepository;
	private final CourseRepository courseRepository;
	
	
	EnrollmentController(EnrollmentRepository enrollmentRepository, UserRepository userRepository, CourseRepository courseRepository, EnrollmentService enrollmentService) {
		this.enrollmentRepository = enrollmentRepository;
		this.enrollmentService = enrollmentService;
		this.userRepository = userRepository;
		this.courseRepository = courseRepository;
	}
	
	@PostMapping("/courses/{code}/enroll")
    ResponseEntity<Void> newEnrollment(@PathVariable("code") String code, @RequestBody User userBody) {
		Course course = courseRepository.findByCode(code)
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("Course %s not found", code)));		
		
		User user = userRepository.findByUsername(userBody.getUsername())
				.orElseThrow(() -> new ResponseStatusException(NOT_FOUND, format("User %s not found", userBody.getUsername())));
		
		enrollmentService.validateUserEnrollment(user, course);
		
		Enrollment enrollment = new Enrollment(user, course);		
		enrollmentRepository.save(enrollment);
		
		user.enroll(enrollment);
		course.enroll(enrollment);
		
		return ResponseEntity.status(HttpStatus.CREATED).build();
    }
	
	@GetMapping("/courses/enroll/report")
	ResponseEntity<?> getEnrollmentsReport() {
		List<Enrollment> enrollments = enrollmentRepository.findAll();
		List<User> users = userRepository.findAll();
		
		if (enrollments.isEmpty() || users.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		
		List<EnrollmentReportResponse> reports = enrollmentService.toReport(enrollments, users);
		return ResponseEntity.ok(reports);
	}

}
