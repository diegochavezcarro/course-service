# Course Service Project

## Overview

This project is a Spring Boot application that manages courses. It provides RESTful HTTP services for handling `Course` entities.

## Testing Overview

### Unit Testing

**Unit Testing** involves testing individual components of a software application in isolation to ensure they work as expected. These tests are typically written by developers and run frequently to catch bugs early in the development cycle.

**Benefits of Unit Testing:**
- Identifies issues early in the development process.
- Provides documentation for the code.
- Facilitates changes and simplifies integration.

**Example of a Unit Test:**
```java
@Test
public void testGetCourseById() {
    Course course = new Course(1L, "Course 1", "Description 1");

    when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

    Optional<Course> foundCourse = courseService.getCourseById(1L);

    assertThat(foundCourse.isPresent()).isTrue();
    assertThat(foundCourse.get().getName()).isEqualTo("Course 1");

    verify(courseRepository, times(1)).findById(1L);
}
