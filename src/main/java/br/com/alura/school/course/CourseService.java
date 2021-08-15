package br.com.alura.school.course;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
public class CourseService {

	public List<CourseResponse> convertList(List<Course> courseList) {
		return courseList.stream().map(CourseResponse::new).collect(Collectors.toList());
	}
}
