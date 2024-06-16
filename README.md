# Unit, Integration and Mutation Testing Project

## Overview

This project is a Spring Boot application that manages courses. It provides RESTful HTTP services for handling `Course` entities.

## Testing Overview

### Unit Testing

**Unit Testing** involves testing individual components of a software application in isolation to ensure they work as expected. These tests are typically written by developers and run frequently to catch bugs early in the development cycle.

**Benefits of Unit Testing:**
- Identifies issues early in the development process.
- Provides documentation for the code.
- Facilitates changes and simplifies integration.

### Integration Testing
**Integration Testing** involves testing the interactions between different components or modules of an application to ensure they work together correctly. These tests are typically more extensive than unit tests and may involve a real or simulated environment.

**Benefits of Integration Testing:**

Validates the interaction between integrated components.
Detects interface issues between modules.
Provides end-to-end testing scenarios.

### The Testing Pyramid
The Testing Pyramid is a conceptual framework that highlights the different levels of testing required for a robust software application. It emphasizes having a large number of unit tests, fewer integration tests, and even fewer end-to-end tests.

![alt](test-pyramid.png)

**Explanation of the Testing Pyramid:**

**Unit Tests:** Form the base of the pyramid. They are fast, numerous, and provide a solid foundation by testing individual components in isolation.
**Integration Tests:** Make up the middle layer. They test the interactions between components and are fewer in number compared to unit tests.
**End-to-End Tests:** Sit at the top of the pyramid. They test the application as a whole, from the user interface to the data layer. These tests are fewer because they are slower and more complex to maintain.