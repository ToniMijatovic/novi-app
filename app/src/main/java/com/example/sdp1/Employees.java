package com.example.sdp1;

import java.util.ArrayList;

public class Employees {
    public ArrayList<Employee> employees = new ArrayList();

    public void setEmployee(Employee employee) {
        this.employees.add(employee);
    }
    public ArrayList<Employee> getEmployees(){
        return this.employees;
    }
}
