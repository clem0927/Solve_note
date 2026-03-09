package min.chat.child.grade.controller;

import lombok.RequiredArgsConstructor;
import min.chat.child.grade.dto.GradeDto;
import min.chat.child.grade.service.GradeService;
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