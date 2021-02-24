package com.student.eksamen.Controller;

import com.student.eksamen.Model.Student;
import com.student.eksamen.Repository.StudentRepo;
import com.student.eksamen.Repository.SupervisorRepo;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RestStudentControllerTest {

    StudentRepo mockedStudentRepo;
    SupervisorRepo mockedSupervisorRepo;
    RestStudentController mockedStudentController;

    @BeforeEach
    public void initMocks(){
        mockedStudentRepo = mock(StudentRepo.class);
        mockedSupervisorRepo = mock(SupervisorRepo.class);
        mockedStudentController = new RestStudentController(mockedStudentRepo, mockedSupervisorRepo);
    }

    @Test
    public void getStudentbyIDTest() throws IOException {
        Long id = 1L;
        when(mockedStudentRepo.findById(id)).thenReturn(Optional.of(singleStudentFetcher()));
        assertEquals("Dixie Harper", mockedStudentRepo.findById(id).get().getStudentName());
        assertNotEquals("Dixie Harper2", mockedStudentRepo.findById(id).get().getStudentName());
    }

    private Student singleStudentFetcher() throws FileNotFoundException {
        JsonReader studentReader = new JsonReader(new FileReader(new File("src/test/resources/studentTest.json")));
        return new Gson().fromJson(studentReader, Student.class);
    }

}