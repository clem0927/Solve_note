package kr.solvenote.grade.controller;

import lombok.RequiredArgsConstructor;
import kr.solvenote.grade.dto.GradeDto;
import kr.solvenote.grade.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<Void> createGrade(@RequestBody GradeDto dto) {

        gradeService.createGrade(dto);

        return ResponseEntity.ok().build();
    }

}