package com.diego.courses.courseservice;

import com.diego.courses.courseservice.model.Course;
import com.diego.courses.courseservice.repository.CourseRepository;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@Tag("integration")
public class CourseServiceIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepository courseRepository;

    @BeforeEach
    public void setUp() {
        courseRepository.deleteAll();
    }

    @Test
    public void testGetAllCourses() {
        courseRepository.save(new Course(null, "Course 1", "Description 1"));
        courseRepository.save(new Course(null, "Course 2", "Description 2"));

        ResponseEntity<Course[]> response = restTemplate.getForEntity(createURL("/api/courses"), Course[].class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).hasSize(2);
    }

    @Test
    public void testGetCourseById() {
        Course savedCourse = courseRepository.save(new Course(null, "Course 1", "Description 1"));

        ResponseEntity<Course> response = restTemplate.getForEntity(createURL("/api/courses/" + savedCourse.getId()),
                Course.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Course 1");
    }

    @Test
    public void testGetCourseByIdNotFound() {
        ResponseEntity<Course> response = restTemplate.getForEntity(createURL("/api/courses/1"), Course.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testCreateCourse() {
        Course course = new Course(null, "Course 1", "Description 1");

        ResponseEntity<Course> response = restTemplate.postForEntity(createURL("/api/courses"), course, Course.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Course 1");

        Course foundCourse = courseRepository.findById(response.getBody().getId()).orElse(null);
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getName()).isEqualTo("Course 1");
    }

    @Test
    public void testUpdateCourse() {
        Course savedCourse = courseRepository.save(new Course(null, "Course 1", "Description 1"));

        Course updatedCourse = new Course(null, "Updated Course", "Updated Description");

        HttpEntity<Course> requestEntity = new HttpEntity<>(updatedCourse);
        ResponseEntity<Course> response = restTemplate.exchange(createURL("/api/courses/" + savedCourse.getId()),
                HttpMethod.PUT, requestEntity, Course.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getName()).isEqualTo("Updated Course");

        Course foundCourse = courseRepository.findById(savedCourse.getId()).orElse(null);
        assertThat(foundCourse).isNotNull();
        assertThat(foundCourse.getName()).isEqualTo("Updated Course");
        assertThat(foundCourse.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    public void testUpdateCourseNotFound() {
        Course updatedCourse = new Course(null, "Updated Course", "Updated Description");

        HttpEntity<Course> requestEntity = new HttpEntity<>(updatedCourse);
        ResponseEntity<Course> response = restTemplate.exchange(createURL("/api/courses/1"), HttpMethod.PUT,
                requestEntity, Course.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteCourse() {
        Course savedCourse = courseRepository.save(new Course(null, "Course 1", "Description 1"));

        ResponseEntity<Void> response = restTemplate.exchange(createURL("/api/courses/" + savedCourse.getId()),
                HttpMethod.DELETE, null, Void.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(courseRepository.findById(savedCourse.getId())).isEmpty();
    }

    private String createURL(String uri) {
        return "http://localhost:" + port + uri;
    }
}
