package br.com.alura.school.enrollment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {

	Enrollment findByUser_UsernameAndCourse_Code(String username, String code);
}
