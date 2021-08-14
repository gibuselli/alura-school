package br.com.alura.school.enrollment;

import static javax.persistence.GenerationType.IDENTITY;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.com.alura.school.course.Course;
import br.com.alura.school.user.User;

@Entity
public class Enrollment {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    
    @ManyToOne
    @NotBlank
    private User user;
    
    @ManyToOne
    @NotBlank
    private Course course;
    
    @Column(nullable = false)
	@JsonFormat(pattern="dd-MM-yyyy")
	private LocalDate enrollDate = LocalDate.now();
    
    @Deprecated
    protected Enrollment() {}       

	public Enrollment(Long id, @NotBlank User user, @NotBlank Course course) {
		this.user = user;
		this.course = course;
	}

	public Long getId() {
		return id;
	}

	public User getUser() {
		return user;
	}

	public Course getCourse() {
		return course;
	}

	public LocalDate getEnrollDate() {
		return enrollDate;
	}    
    
    
    
}
