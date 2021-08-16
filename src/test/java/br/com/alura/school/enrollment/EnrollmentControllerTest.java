package br.com.alura.school.enrollment;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.alura.school.course.Course;
import br.com.alura.school.course.CourseRepository;
import br.com.alura.school.user.User;
import br.com.alura.school.user.UserRepository;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class EnrollmentControllerTest {

	private final ObjectMapper jsonMapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private EnrollmentRepository enrollmentRepository;
    
    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private UserRepository userRepository;
    
    @Test
    void should_add_new_enrollment() throws Exception {
    	
        User user = new User("michael", "michael@email.com");
        Course course = new Course("java-1", "Modern Java", "Java Lessons");
        userRepository.save(user);
        courseRepository.save(course);
        NewEnrollmentRequest enrollmentRequest = new NewEnrollmentRequest("michael");


        mockMvc.perform(post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isCreated());
    }
    
    @Test
	void should_return_bad_request_if_user_is_already_enrolled() throws Exception {
    	User michael = new User("michael", "michael@email.com");
    	Course java1 = new Course("java-1", "Java Basics", "Java Lessons");
    	userRepository.save(michael);
    	courseRepository.save(java1);
    	Enrollment enrollmentMichael1 = new Enrollment(michael, java1);
    	enrollmentRepository.save(enrollmentMichael1);
    	NewEnrollmentRequest enrollmentRequest = new NewEnrollmentRequest("michael");
    	
        mockMvc.perform(post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isBadRequest());
	}
    
    @Test
	void should_return_not_found_if_user_not_exists() throws Exception {
    	Course java1 = new Course("java-1", "Java Basics", "Java Lessons");
    	courseRepository.save(java1);
    	NewEnrollmentRequest enrollmentRequest = new NewEnrollmentRequest("michael");
    	
        mockMvc.perform(post("/courses/java-1/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonMapper.writeValueAsString(enrollmentRequest)))
                .andExpect(status().isNotFound());
	}
    
    @Test
    void should_return_not_found_if_course_not_exists() throws Exception {
    	Course java1 = new Course("java-1", "Java Basics", "Java Lessons");
    	User michael = new User("michael", "michael@email.com");
    	userRepository.save(michael);
    	courseRepository.save(java1);
    	NewEnrollmentRequest enrollmentRequest = new NewEnrollmentRequest("michael");
    	
    	mockMvc.perform(post("/courses/java-2/enroll")
    			.contentType(MediaType.APPLICATION_JSON)
    			.content(jsonMapper.writeValueAsString(enrollmentRequest)))
    			.andExpect(status().isNotFound());
    }
    
    @Test
    void should_return_enroll_report() throws Exception {
    	User user = new User("michael", "michael@email.com");
        Course course = new Course("java-1", "Modern Java", "Java Lessons");
        userRepository.save(user);
        courseRepository.save(course);
        Enrollment enrollment = new Enrollment(user, course);
        enrollmentRepository.save(enrollment);
    	
    	mockMvc.perform(get("/courses/enroll/report")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is("michael@email.com")))
                .andExpect(jsonPath("$[0].quantidade_matriculas", is(1)));
    }
    
    @Test
    void should_return_enroll_report_ordered_by_enroll_amount_desc() throws Exception {
    	User michael = new User("michael", "michael@email.com");
    	User jim = new User("jim", "jim@email.com");
    	User pam = new User("pam", "pam@email.com");
        Course java1 = new Course("java-1", "Java Basics", "Java Lessons");
        Course java2 = new Course("java-2", "Java OO", "Java Lessons");
        Course java3 = new Course("java-3", "Modern Java", "Java Lessons");
        userRepository.save(michael);
        userRepository.save(jim);
        userRepository.save(pam);
        courseRepository.save(java1);
        courseRepository.save(java2);
        courseRepository.save(java3);
        Enrollment enrollmentMichael1 = new Enrollment(michael, java1);
        Enrollment enrollmentMichael2 = new Enrollment(michael, java2);
        Enrollment enrollmentMichael3 = new Enrollment(michael, java3);
        Enrollment enrollmentPam1 = new Enrollment(pam, java1);
        Enrollment enrollmentPam2 = new Enrollment(pam, java2);
        Enrollment enrollmentJim1 = new Enrollment(jim, java1);
        enrollmentRepository.save(enrollmentMichael1);
        enrollmentRepository.save(enrollmentMichael2);
        enrollmentRepository.save(enrollmentMichael3);
        enrollmentRepository.save(enrollmentPam1);
        enrollmentRepository.save(enrollmentPam2);
        enrollmentRepository.save(enrollmentJim1);
    	
    	mockMvc.perform(get("/courses/enroll/report")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].email", is("michael@email.com")))
                .andExpect(jsonPath("$[0].quantidade_matriculas", is(3)))
		    	.andExpect(jsonPath("$[1].email", is("pam@email.com")))
                .andExpect(jsonPath("$[1].quantidade_matriculas", is(2)))
		    	.andExpect(jsonPath("$[2].email", is("jim@email.com")))
                .andExpect(jsonPath("$[2].quantidade_matriculas", is(1)));

    }    

    
    @Test
	void should_return_no_content_if_empty_enroll_report() throws Exception {
    	
    	mockMvc.perform(get("/courses/enroll/report")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
	}
        
    

}
