package com.mandar.SpringSecurity.controllers;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mandar.SpringSecurity.models.Student;

@RestController
public class StudentController {

    private List<Student> studentlist = new ArrayList<>(List.of(
            new Student(1, "Mandar", 80),
            new Student(2, "Makarand", 85),
            new Student(3, "Vijaya", 90)));

    @GetMapping("/students")
    public List<Student> getStudents() {

        return studentlist;

    }

    @PostMapping("/students")
    public Student addStudent(@RequestBody Student student){
        studentlist.add(student);
        return student;
    }


}
