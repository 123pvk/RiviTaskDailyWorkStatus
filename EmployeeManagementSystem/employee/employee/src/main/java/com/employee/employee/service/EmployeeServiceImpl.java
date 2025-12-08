package com.employee.employee.service;


import com.employee.employee.dto.EmployeeDto;
import com.employee.employee.entity.Employee;
import com.employee.employee.exception.EmployeeNotFoundException;
import com.employee.employee.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl  implements  EmployeeService{


    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    private EmployeeDto mapToDto(Employee employee) {
        return new EmployeeDto(employee.getId(),
                               employee.getFirstName(),
                               employee.getLastName(),
                               employee.getDepartment(),
                               employee.getSalary());

    }

    private Employee mapToEntity(EmployeeDto employeeDto) {
        return new Employee(employeeDto.getId(),
                            employeeDto.getFirstName(),
                            employeeDto.getLastName(),
                            employeeDto.getDepartment(),
                            employeeDto.getSalary()
                );
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto dto) {
        Employee employee = mapToEntity(dto);
        Employee savedEmployee = employeeRepository.save(employee);
        return mapToDto(savedEmployee);
    }


    public EmployeeDto getEmployeeById(Long id) {
         Employee employee = employeeRepository.findById(id)
            .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));
    return mapToDto(employee);
    }

    @Override
    public List<EmployeeDto> getAllEmployees() {
        return employeeRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmployeeDto updateEmployee(Long id, EmployeeDto dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        employee.setFirstName(dto.getFirstName());
        employee.setLastName(dto.getLastName());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());

        return mapToDto(employeeRepository.save(employee));
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee not found with id: " + id));

        employeeRepository.delete(employee);
    }





}
