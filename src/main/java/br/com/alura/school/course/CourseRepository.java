package br.com.alura.school.course;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface CourseRepository extends JpaRepository<Course, Long> {
    Optional<Course> findByCode(String code);
}
