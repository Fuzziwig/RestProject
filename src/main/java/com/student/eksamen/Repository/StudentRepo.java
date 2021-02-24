package com.student.eksamen.Repository;

import com.student.eksamen.Model.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepo extends CrudRepository<Student, Long> {
}
