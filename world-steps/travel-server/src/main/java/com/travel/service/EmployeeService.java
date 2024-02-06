package com.travel.service;

import com.travel.dto.EmployeeLoginDTO;
import com.travel.entity.Employee;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

}
