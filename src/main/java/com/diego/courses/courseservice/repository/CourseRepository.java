package com.diego.courses.courseservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.diego.courses.courseservice.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
}
