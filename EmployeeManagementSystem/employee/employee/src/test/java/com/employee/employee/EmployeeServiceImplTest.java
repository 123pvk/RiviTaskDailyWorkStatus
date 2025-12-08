package com.employee.employee;



import com.employee.employee.dto.EmployeeDto;
import com.employee.employee.entity.Employee;
import com.employee.employee.exception.EmployeeNotFoundException;
import com.employee.employee.repository.EmployeeRepository;
import com.employee.employee.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    private Employee employee;
    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        employee = new Employee(1L, "John", "Doe", "IT", 5000.0);
        employeeDto = new EmployeeDto(1L, "John", "Doe", "IT", 5000.0);
    }

    // ---------------------------------------------------------
    // CREATE EMPLOYEE
    // ---------------------------------------------------------
    @Test
    void testCreateEmployee() {
        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        EmployeeDto saved = employeeService.createEmployee(employeeDto);

        assertNotNull(saved);
        assertEquals("John", saved.getFirstName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    // ---------------------------------------------------------
    // GET EMPLOYEE BY ID
    // ---------------------------------------------------------
    @Test
    void testGetEmployeeById_Found() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        EmployeeDto result = employeeService.getEmployeeById(1L);

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        verify(employeeRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEmployeeById_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> employeeService.getEmployeeById(1L));
    }

    // ---------------------------------------------------------
    // GET ALL EMPLOYEES
    // ---------------------------------------------------------
    @Test
    void testGetAllEmployees() {
        List<Employee> employeeList = Arrays.asList(
                new Employee(1L, "John", "Doe", "IT", 5000.0),
                new Employee(2L, "Jane", "Smith", "HR", 4500.0)
        );

        when(employeeRepository.findAll()).thenReturn(employeeList);

        List<EmployeeDto> result = employeeService.getAllEmployees();

        assertEquals(2, result.size());
        verify(employeeRepository, times(1)).findAll();
    }

    // ---------------------------------------------------------
    // UPDATE EMPLOYEE
    // ---------------------------------------------------------
    @Test
    void testUpdateEmployee() {
        Employee updatedEmployee = new Employee(1L, "Mike", "Ross", "Sales", 6000.0);
        EmployeeDto updateDto = new EmployeeDto(null, "Mike", "Ross", "Sales", 6000.0);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(employeeRepository.save(any(Employee.class))).thenReturn(updatedEmployee);

        EmployeeDto result = employeeService.updateEmployee(1L, updateDto);

        assertEquals("Mike", result.getFirstName());
        verify(employeeRepository, times(1)).save(any(Employee.class));
    }

    @Test
    void testUpdateEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.updateEmployee(1L, employeeDto));
    }

    // ---------------------------------------------------------
    // DELETE EMPLOYEE
    // ---------------------------------------------------------
    @Test
    void testDeleteEmployee() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).delete(employee);
    }

    @Test
    void testDeleteEmployee_NotFound() {
        when(employeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EmployeeNotFoundException.class,
                () -> employeeService.deleteEmployee(1L));
    }
}
