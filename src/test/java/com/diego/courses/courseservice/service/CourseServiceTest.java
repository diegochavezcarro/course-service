package com.diego.courses.courseservice.service;

import com.diego.courses.courseservice.model.Course;
import com.diego.courses.courseservice.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

public class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private CourseService courseService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllCourses() {
        Course course1 = new Course(1L, "Course 1", "Description 1");
        Course course2 = new Course(2L, "Course 2", "Description 2");

        when(courseRepository.findAll()).thenReturn(Arrays.asList(course1, course2));

        List<Course> courses = courseService.getAllCourses();

        assertThat(courses).hasSize(2);
        assertThat(courses.get(0).getName()).isEqualTo("Course 1");
        assertThat(courses.get(1).getName()).isEqualTo("Course 2");

        verify(courseRepository, times(1)).findAll();
    }

    @Test
    public void testGetCourseById() {
        Course course = new Course(1L, "Course 1", "Description 1");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Optional<Course> foundCourse = courseService.getCourseById(1L);

        assertThat(foundCourse.isPresent()).isTrue();
        assertThat(foundCourse.get().getName()).isEqualTo("Course 1");

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetCourseByIdNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Course> foundCourse = courseService.getCourseById(1L);

        assertThat(foundCourse.isPresent()).isFalse();

        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    public void testSaveCourse() {
        Course course = new Course(1L, "Course 1", "Description 1");

        when(courseRepository.save(any(Course.class))).thenReturn(course);

        Course savedCourse = courseService.saveCourse(course);

        assertThat(savedCourse.getName()).isEqualTo("Course 1");

        verify(courseRepository, times(1)).save(course);
    }

    @Test
    public void testDeleteCourse() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }
}

