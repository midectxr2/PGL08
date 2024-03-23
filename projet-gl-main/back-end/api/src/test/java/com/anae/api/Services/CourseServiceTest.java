package com.anae.api.Services;

import com.anae.api.Entities.Course;
import com.anae.api.Repository.CourseDataRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {
    @Mock
    private CourseDataRepository courseDataRepository;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        courseService = new CourseService(courseDataRepository);
    }

    @AfterEach
    void tearDown() {
        courseDataRepository.deleteAll();
    }

    @Test
    void itShouldSuccessfullyAddACourse() {
        //given
        Course course = new Course();
        course.setCourseId("SC-INFO");
        course.setTitle("Sciences Informatiques");

        //when
        courseService.addCourse(course);

        //then
        ArgumentCaptor<Course> courseArgumentCaptor = ArgumentCaptor.forClass(Course.class);

        verify(courseDataRepository).save(courseArgumentCaptor.capture());

        Course capturedCourse = courseArgumentCaptor.getValue();

        assertThat(capturedCourse).isEqualTo(course);
    }

    @Test
    void itShouldGetAllCourses() {
        courseService.getCourses();
        verify(courseDataRepository).findAll();
    }

    @Test
    void getCourse() {
        //given
        String id = "SC-INFO";
        Course testCourse = new Course();
        testCourse.setCourseId(id);

        given(courseDataRepository.findByCourseId(id)).willReturn(Optional.of(testCourse));

        //when
        courseService.getCourse(id);

        //then
        verify(courseDataRepository).findByCourseId(any());
    }

    @Test
    void isLinkedCursus() {
        String courseId = "SC-INFO", cursusId = "test";

        courseService.isLinkedCursus(courseId, cursusId);

        verify(courseDataRepository).findByCourseIdAndLinkedCursusCursusId(courseId, cursusId);
    }
}