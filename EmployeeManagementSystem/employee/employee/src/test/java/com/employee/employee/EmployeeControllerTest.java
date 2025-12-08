package com.employee.employee;



import com.employee.employee.controller.EmployeeController;
import com.employee.employee.dto.EmployeeDto;
import com.employee.employee.exception.EmployeeNotFoundException;
import com.employee.employee.exception.GlobalExceptionHandler;
import com.employee.employee.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeService;

    @InjectMocks
    private EmployeeController employeeController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders
                .standaloneSetup(employeeController)
                .setControllerAdvice(new GlobalExceptionHandler()) // <--- Add this
                .build();
        objectMapper = new ObjectMapper();
    }


    @Test
    void testCreateEmployee() throws Exception {

        EmployeeDto request = new EmployeeDto(1L, "John", "Hockey", "IT", 50000);
        when(employeeService.createEmployee(any(EmployeeDto.class))).thenReturn(request);

        mockMvc.perform(post("/api/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.department").value("IT"));

        verify(employeeService).createEmployee(any(EmployeeDto.class));
    }

    @Test
    void testGetEmployeeById() throws Exception {

        EmployeeDto dto = new EmployeeDto(1L, "John", "Hockey", "IT", 50000);

        when(employeeService.getEmployeeById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.department").value("IT"));

        verify(employeeService).getEmployeeById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() throws Exception {

        when(employeeService.getEmployeeById(1L))
                .thenThrow(new EmployeeNotFoundException("Employee not found with id: 1"));

        mockMvc.perform(get("/api/employees/1"))
                .andExpect(status().isNotFound()); // now returns 404

        verify(employeeService).getEmployeeById(1L);
    }



    @Test
    void testGetAllEmployees() throws Exception {

        List<EmployeeDto> list = List.of(
                new EmployeeDto(1L, "John", "Hockey", "IT", 50000),
                new EmployeeDto(2L, "Mark", "Smith", "HR", 45000)
        );

        when(employeeService.getAllEmployees()).thenReturn(list);

        mockMvc.perform(get("/api/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(employeeService).getAllEmployees();
    }

    @Test
    void testUpdateEmployee() throws Exception {

        EmployeeDto updated = new EmployeeDto(1L, "Updated", "User", "IT", 60000);

        when(employeeService.updateEmployee(eq(1L), any(EmployeeDto.class))).thenReturn(updated);

        mockMvc.perform(put("/api/employees/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updated)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));

        verify(employeeService).updateEmployee(eq(1L), any(EmployeeDto.class));
    }

    @Test
    void testDeleteEmployee() throws Exception {

        doNothing().when(employeeService).deleteEmployee(1L);

        mockMvc.perform(delete("/api/employees/1"))
                .andExpect(status().isNoContent());

        verify(employeeService).deleteEmployee(1L);
    }
}

