package com.student.eksamen.Controller;

import com.student.eksamen.Model.Student;
import com.student.eksamen.Model.Supervisor;
import com.student.eksamen.Repository.StudentRepo;
import com.student.eksamen.Repository.SupervisorRepo;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * The type Rest student controller.
 */
@RestController
public class RestStudentController {

    private StudentRepo studentRepo;
    private SupervisorRepo supervisorRepo;

    /**
     * Instantiates a new Rest student controller.
     *
     * @param studentRepo    the student repo
     * @param supervisorRepo the supervisor repo
     */
    public RestStudentController(StudentRepo studentRepo, SupervisorRepo supervisorRepo) {
        this.studentRepo = studentRepo;
        this.supervisorRepo = supervisorRepo;
    }

    /**
     * Find all iterable. (HTTP Get List)
     *
     * @return the student iterable
     */
    @GetMapping("/student")
    public Iterable<Student> findAll(){
        return studentRepo.findAll();
    }

    /**
     * Find by student id response entity. (HTTP Get By ID)
     *
     * @param id the student id
     * @return the response entity
     */
    @GetMapping("/student/{id}")
    public ResponseEntity<Optional<Student>> findById(@PathVariable Long id){
        //find the student
        Optional<Student> student = studentRepo.findById(id);
        //if student is present say Ok else Not Found
        if (student.isPresent()){
            return ResponseEntity.status(200).body(student); //OK
        } else {
            return ResponseEntity.status(404).body(student); //Not Found
        }
    }

    /**
     * Create student response entity. (HTTP Post, ie. create)
     *
     * @param p the student
     * @return the response entity
     */
    @PostMapping(value = "/student", consumes = {"application/json"})
    public ResponseEntity<String> create(@RequestBody Student p){
        Student student = new Student(p.getStudentName(), p.getEmail(), p.getSupervisor());
        studentRepo.save(student);
        return ResponseEntity.status(201).header("Location", "/student/" + p.getId()).body("{'Msg': 'post created'}");
    }

    /**
     * Update Student response entity. (HTTP PUT, ie. update)
     *
     * @param id the student id
     * @param p  the student
     * @return the response entity
     */
    @PutMapping("/student/{id}")
    public ResponseEntity<String> update(@PathVariable("id") Long id, @RequestBody Student p){
        //find student
        Optional<Student> optionalStudent = studentRepo.findById(id);
        //if student is present say Ok else Not Found
        if (!optionalStudent.isPresent()){
            return ResponseEntity.status(404).body("{'msg':'Not found'");
        }
        //set the student id before saving because its not in our request body
        p.setId(id);
        studentRepo.save(p);
        return ResponseEntity.status(204).body("{'msg':'Updated'}");
    }

    /**
     * Delete Student response entity. (HTTP Delete)
     *
     * @param id the student id
     * @return the response entity
     */
    @DeleteMapping("/student/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id){
        Optional<Student> student = studentRepo.findById(id);
        //if student is present say Ok else Not Found
        if(!student.isPresent()){
            return ResponseEntity.status(404).body("{'msg':'Not found'"); // Not found
        }
        Student p = student.get();
        //deleting a student also means removing supervisors references to the student
        Supervisor supervisor = p.getSupervisor();
        for (Student s : supervisor.getStudents()){
            if (s.getId()==p.getId()){
                supervisor.getStudents().remove(s);
            }
        }
        supervisorRepo.save(supervisor);
        studentRepo.save(p);
        studentRepo.deleteById(id);
        return ResponseEntity.status(200).body("{'msg':'Deleted'}");
    }

}
