package com.anae.api.Services;

import com.anae.api.DTOs.CursusDTO;
import com.anae.api.Entities.Cursus;
import com.anae.api.Repository.CursusDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CursusService {
    private final CursusDataRepository cursusDataRepository;
    private final CourseService courseService;

    public CursusService(CursusDataRepository cursusDataRepository, CourseService courseService) {
        this.cursusDataRepository = cursusDataRepository;
        this.courseService = courseService;
    }

    public void registerCursus(String id, String title) {
        cursusDataRepository.save(new Cursus(id, title));
    }

    public void registerCursus(CursusDTO cursusDTO) {
        registerCursus(cursusDTO.id(), cursusDTO.title());
    }

    public Cursus loadCursus(String id) {
        return cursusDataRepository.findByCursusId(id).orElseThrow();
    }

    public void saveCursus(Cursus cursus) {
        cursusDataRepository.save(cursus);
    }

    public List<Cursus> getAll() {
        return cursusDataRepository.findAll();
    }

    public boolean addCourse(Cursus cursus, String courseId) {
        var course = courseService.getCourse(courseId);

        cursus.getCourses().add(course);

        try {
            cursusDataRepository.save(cursus);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean removeCourse(Cursus cursus, String courseId) {
        if (courseService.isLinkedCursus(courseId, cursus.getCursusId())) {
            var course = courseService.getCourse(courseId);
            cursus.getCourses().remove(course);
            cursusDataRepository.save(cursus);
            return true;
        }

        return false;
    }

    public boolean existById(String cursusId) {
        return cursusDataRepository.existsByCursusId(cursusId);
    }
}
