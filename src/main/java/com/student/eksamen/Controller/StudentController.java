package com.student.eksamen.Controller;

import com.student.eksamen.Model.Student;
import com.student.eksamen.Model.Supervisor;
import com.student.eksamen.Repository.StudentRepo;
import com.student.eksamen.Repository.SupervisorRepo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * The type Student controller.
 */
@Controller
public class StudentController {

    private StudentRepo studentRepo;
    private SupervisorRepo supervisorRepo;

    /**
     * Instantiates a new Student controller.
     *
     * @param studentRepo    the student repo
     * @param supervisorRepo the supervisor repo
     */
    public StudentController(StudentRepo studentRepo, SupervisorRepo supervisorRepo) {
        this.studentRepo = studentRepo;
        this.supervisorRepo = supervisorRepo;
    }

    /**
     * Basic student page.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/")
    public String basic (Model model){
        //add attributes that may be needed for basic page
        model.addAttribute("newstudent", new Student());
        model.addAttribute("supervisors", supervisorRepo.findAll());
        model.addAttribute("students", studentRepo.findAll());
        return "basic";
    }

    /**
     * Create student
     *
     * @param newstudent the newstudent
     * @param model      the model
     * @return the string
     */
    @PostMapping("/addStudent")
    public String createStudent(@ModelAttribute Student newstudent, Model model){
        studentRepo.save(newstudent);
        return ("redirect:/basic");
    }

    /**
     * Update student Get.
     *
     * @param id    the student id
     * @param model the model
     * @return the string
     */
    @GetMapping("/editStudent/{id}")
    public String updateStudent(@PathVariable("id") long id, Model model){
        Optional<Student> student = studentRepo.findById(id);
        //if student is present add attributes to the model else return to the basic page
        if (student.isPresent()){
            model.addAttribute("student", student.get());
            Long supervisor_id = student.get().getSupervisor().getId();
            model.addAttribute("supervisor_id",supervisor_id);
            return ("editstudent");
        }
        return "basic";
    }

    /**
     * Update student Post
     *
     * @param id      the student id
     * @param student the student
     * @param model   the model
     * @return the string
     */
    @PostMapping("/updateStudent/{id}")
    public String updateStudentPost(@PathVariable("id") int id, @ModelAttribute Student student, Model model){
        studentRepo.save(student);
        model.addAttribute("students", studentRepo.findAll());
        return ("redirect:/basic");
    }


    /**
     * Delete student
     *
     * @param id the student id
     * @return the string
     */
    @RequestMapping(value = "/deleteStudent/{id}", method = RequestMethod.GET)
    public String deleteStudent(@PathVariable long id) {
        Optional<Student> student = studentRepo.findById(id);
        Student p = student.get();
        Supervisor supervisor = p.getSupervisor();
        //remove supervisor references to the student that is being deleted
        for (Student s : supervisor.getStudents()){
            if (s.getId()==p.getId()){
                supervisor.getStudents().remove(s);
            }
        }
        supervisorRepo.save(supervisor);
        studentRepo.save(p);
        studentRepo.deleteById(id);
        return "redirect:/basic";
    }

}



