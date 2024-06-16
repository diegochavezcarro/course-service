package com.diego.courses.courseservice.controller;

import com.diego.courses.courseservice.model.Course;
import com.diego.courses.courseservice.service.CourseService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.ArgumentCaptor;
import static org.assertj.core.api.Assertions.assertThat;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
@Tag("unit")
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Test
    public void testGetAllCourses() throws Exception {
        Course course1 = new Course(1L, "Course 1", "Description 1");
        Course course2 = new Course(2L, "Course 2", "Description 2");

        when(courseService.getAllCourses()).thenReturn(Arrays.asList(course1, course2));

        mockMvc.perform(get("/api/courses")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Course 1"))
                .andExpect(jsonPath("$[1].name").value("Course 2"));
    }

    @Test
    public void testGetCourseById() throws Exception {
        Course course = new Course(1L, "Course 1", "Description 1");

        when(courseService.getCourseById(1L)).thenReturn(Optional.of(course));

        mockMvc.perform(get("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Course 1"));
    }

    @Test
    public void testGetCourseByIdNotFound() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateCourse() throws Exception {
        Course course = new Course(1L, "Course 1", "Description 1");

        when(courseService.saveCourse(any(Course.class))).thenReturn(course);

        mockMvc.perform(post("/api/courses")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Course 1\",\"description\":\"Description 1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Course 1"));
    }

    @Test
    public void testUpdateCourse() throws Exception {
        Course existingCourse = new Course(1L, "Course 1", "Description 1");
        Course updatedCourse = new Course(1L, "Updated Course", "Updated Description");

        when(courseService.getCourseById(1L)).thenReturn(Optional.of(existingCourse));
        when(courseService.saveCourse(any(Course.class))).thenReturn(updatedCourse);

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Course\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Course"))
                .andExpect(jsonPath("$.description").value("Updated Description"));

        verify(courseService, times(1)).getCourseById(1L);
        verify(courseService, times(1)).saveCourse(any(Course.class));

        // Capture the argument passed to saveCourse
        ArgumentCaptor<Course> courseCaptor = ArgumentCaptor.forClass(Course.class);
        verify(courseService).saveCourse(courseCaptor.capture());
        Course savedCourse = courseCaptor.getValue();

        // Verify that the fields have been updated
        assertThat(savedCourse.getName()).isEqualTo("Updated Course");
        assertThat(savedCourse.getDescription()).isEqualTo("Updated Description");
    }

    @Test
    public void testUpdateCourseNotFound() throws Exception {
        when(courseService.getCourseById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"Updated Course\",\"description\":\"Updated Description\"}"))
                .andExpect(status().isNotFound());

        verify(courseService, times(1)).getCourseById(1L);
        verify(courseService, times(0)).saveCourse(any(Course.class));
    }

    @Test
    public void testDeleteCourse() throws Exception {
        doNothing().when(courseService).deleteCourse(1L);

        mockMvc.perform(delete("/api/courses/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(courseService, times(1)).deleteCourse(1L);
    }
}
