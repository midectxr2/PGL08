package com.anae.api.Controllers;

import com.anae.api.DTOs.CursusDTO;
import com.anae.api.Mapper.CursusDTOMapper;
import com.anae.api.Services.CursusService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/cursus")
public class CursusController {
    private final CursusService cursusService;
    private final CursusDTOMapper cursusDTOMapper;

    public CursusController(CursusService cursusService, CursusDTOMapper cursusDTOMapper) {
        this.cursusService = cursusService;
        this.cursusDTOMapper = cursusDTOMapper;
    }

    @GetMapping
    public ResponseEntity<List<CursusDTO>> getAll() {
        return ResponseEntity.ok(
                cursusService.getAll()
                        .stream().map(cursusDTOMapper).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CursusDTO> getCursus(@PathVariable String id) {
        var cursus = cursusService.loadCursus(id);
        return ResponseEntity.ok(cursusDTOMapper.apply(cursus));
    }

    @PostMapping("/add")
    public ResponseEntity<String> register(@RequestBody CursusDTO cursusDTO) {
        if (!cursusService.existById(cursusDTO.id())) {
            cursusService.registerCursus(cursusDTO);

            return new ResponseEntity<>("Cursus registered", HttpStatus.CREATED);
        }
        return ResponseEntity.ok("Cursus already exists");
    }

    @PostMapping("/{cursusId}/change")
    public ResponseEntity<String> changeProperties(
            @PathVariable String cursusId,
            @RequestParam(defaultValue = "") String newId,
            @RequestParam(defaultValue = "") String newTitle
    ) {
        var cursus = cursusService.loadCursus(cursusId);

        //Check from frontend that both a not simultaneously empty
        if (!newId.isEmpty())
            cursus.setCursusId(newId);

        if (!newTitle.isEmpty())
            cursus.setTitle(newTitle);

        cursusService.saveCursus(cursus);
        return ResponseEntity.ok("Change applied");
    }

    @PostMapping("/{cursusId}/add")
    public ResponseEntity<String> addCourse(
            @PathVariable String cursusId,
            @RequestParam String courseId
    ) {
        var cursus = cursusService.loadCursus(cursusId);

        if (cursusService.addCourse(cursus, courseId))
            return ResponseEntity.ok("Course added to cursus: " + cursusId);

        return ResponseEntity.badRequest().body("Verify given information");
    }

    @PostMapping("/{cursusId}/remove")
    private ResponseEntity<String> remove(
            @PathVariable String cursusId,
            @RequestParam String courseId
    ) {
        var cursus = cursusService.loadCursus(cursusId);

        if (cursusService.removeCourse(cursus, courseId))
            return ResponseEntity.ok("Course removed from cursus: " + cursusId);

        return ResponseEntity.badRequest().body("Verify given information");
    }
}
