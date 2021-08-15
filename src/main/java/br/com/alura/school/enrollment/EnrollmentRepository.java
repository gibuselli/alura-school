package br.com.alura.school.enrollment;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

	Enrollment findByUser_UsernameAndCourse_Code(String username, String code);
}
