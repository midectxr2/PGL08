package com.anae.api.Services;

import com.anae.api.DTOs.CursusDTO;
import com.anae.api.Entities.Course;
import com.anae.api.Entities.Cursus;
import com.anae.api.Repository.CursusDataRepository;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CursusServiceTest {
    @Mock
    private CourseService courseService;
    @Mock
    private CursusDataRepository cursusDataRepository;
    private CursusService cursusService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cursusService = new CursusService(cursusDataRepository, courseService);
    }

    @AfterEach
    void tearDown() {
        cursusDataRepository.deleteAll();
    }

    @Test
    void registerCursus() {
        //given
        CursusDTO cursusDTO = new CursusDTO("id", "title");

        //when
        cursusService.registerCursus(cursusDTO);

        //then
        verify(cursusDataRepository).save(any());
    }

    @Test
    void loadCursus() {
        //given
        Cursus cursus = new Cursus();
        cursus.setCursusId("id");
        cursus.setTitle("title");

        given(cursusDataRepository.findByCursusId("id")).willReturn(Optional.of(cursus));

        //when
        cursusService.loadCursus("id");

        verify(cursusDataRepository).findByCursusId(any());
    }

    @Test
    void saveCursus() {
        //given
        Cursus cursus = new Cursus();
        cursus.setCursusId("id");
        cursus.setTitle("title");

        //when
        cursusService.saveCursus(cursus);

        //then
        ArgumentCaptor<Cursus> cursusArgumentCaptor = ArgumentCaptor.forClass(Cursus.class);

        verify(cursusDataRepository).save(cursusArgumentCaptor.capture());

        Cursus capturedCursus = cursusArgumentCaptor.getValue();

        assertThat(capturedCursus).isEqualTo(cursus);
    }

    @Test
    void getAll() {
        cursusService.getAll();
        verify(cursusDataRepository).findAll();
    }

    @Test
    void addCourseThatShouldWork() {
        //given
        Cursus cursus = new Cursus();
        cursus.setCursusId("id");
        cursus.setTitle("title");

        String courseId = "SC-INFO";


        Course course = new Course();
        course.setCourseId(courseId);
        course.setTitle("Sciences Informatiques");

        given(courseService.getCourse(courseId)).willReturn(course);

        boolean result = cursusService.addCourse(cursus, courseId);

        verify(cursusDataRepository).save(any());
        assertThat(result).isTrue();
    }

    @Test
    void addCourseThatShouldFail() {
        //given
        Cursus cursus = new Cursus();
        cursus.setCursusId("id");
        cursus.setTitle("title");

        String courseId = "SC-INFO";


        Course course = new Course();
        course.setCourseId(courseId);
        course.setTitle("Sciences Informatiques");

        given(courseService.getCourse(courseId)).willReturn(course);
        given(cursusDataRepository.save(cursus)).willThrow(IllegalArgumentException.class);

        boolean result = cursusService.addCourse(cursus, courseId);

        verify(cursusDataRepository).save(any());
        assertThat(result).isFalse();
    }

    @Test
    void shouldRemoveCourseFromCursusWhenIsLinked() {
        //given
        Cursus cursus = new Cursus();
        cursus.setCursusId("id");
        cursus.setTitle("title");

        Course course = new Course();
        course.setCourseId("courseId");
        course.setTitle("courseTitle");

        cursus.getCourses().add(course);

        given(courseService.isLinkedCursus("courseId", "id")).willReturn(true);
        given(courseService.getCourse("courseId")).willReturn(course);

        //when
        boolean result = cursusService.removeCourse(cursus, "courseId");

        //then
        verify(cursusDataRepository).save(cursus);
        assertThat(result).isTrue();
    }

    @Test
    void shouldNotRemoveCourseFromCursusWhenNotLinked() {
        //given
        Cursus cursus = new Cursus();
        cursus.setCursusId("id");
        cursus.setTitle("title");

        Course course = new Course();
        course.setCourseId("courseId");
        course.setTitle("courseTitle");

        cursus.getCourses().add(course);

        given(courseService.isLinkedCursus("courseId", "id")).willReturn(false);

        //when
        boolean result = cursusService.removeCourse(cursus, "courseId");

        //then
        verify(cursusDataRepository, never()).save(cursus);
        assertThat(result).isFalse();
    }

    @Test
    void existById() {
        cursusService.existById("id");
        verify(cursusDataRepository).existsByCursusId("id");
    }
}