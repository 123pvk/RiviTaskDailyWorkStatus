package com.employee.employee;



import com.employee.employee.exception.EmployeeNotFoundException;
import com.employee.employee.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void testHandleEmployeeNotFound() {
        // Arrange
        String errorMessage = "Employee not found with id: 10";
        EmployeeNotFoundException exception = new EmployeeNotFoundException(errorMessage);

        // Act
        ResponseEntity<String> response = handler.handleEmployeeNotFound(exception);

        // Assert
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isEqualTo(errorMessage);
    }
}
