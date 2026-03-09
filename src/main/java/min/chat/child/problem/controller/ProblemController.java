package min.chat.child.problem.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import min.chat.child.problem.dto.ProblemDto;
import min.chat.child.problem.service.ProblemService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @GetMapping
    public ResponseEntity<Page<ProblemDto>> getProblems(
            @RequestParam String email,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<ProblemDto> problems =
                problemService.findProblems(email, keyword, categoryId, page, size);

        return ResponseEntity.ok(problems);
    }

    @PostMapping
    public ResponseEntity<Void> createProblem(@RequestBody ProblemDto dto) {

        problemService.createProblem(dto);

        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProblem(
            @PathVariable Long id,
            @RequestBody ProblemDto dto
    ) {

        problemService.updateProblem(id, dto);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProblem(@PathVariable Long id) {

        problemService.deleteProblem(id);

        return ResponseEntity.ok().build();
    }
    @PutMapping("/{id}/category")
    public ResponseEntity<Void> updateProblemCategory(
            @PathVariable Long id,
            @RequestBody CategoryUpdateRequest request
    ) {

        problemService.updateProblemCategory(id, request.getCategoryId());

        return ResponseEntity.ok().build();
    }

    @Data
    static class CategoryUpdateRequest {

        private Long categoryId; // null이면 전체

    }

}