package kr.solvenote.problemcategory.controller;

import lombok.RequiredArgsConstructor;
import kr.solvenote.problemcategory.dto.ProblemCategoryDto;
import kr.solvenote.problemcategory.service.ProblemCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/problem-category")
@RequiredArgsConstructor
public class ProblemCategoryController {

    private final ProblemCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<ProblemCategoryDto>> getCategories(
            @RequestParam String email
    ) {

        return ResponseEntity.ok(
                categoryService.getCategories(email)
        );

    }

    @PostMapping
    public ResponseEntity<Void> createCategory(
            @RequestBody ProblemCategoryDto dto
    ) {

        categoryService.createCategory(dto);

        return ResponseEntity.ok().build();

    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateCategory(
            @PathVariable Long id,
            @RequestBody ProblemCategoryDto dto
    ) {

        categoryService.updateCategory(id, dto);

        return ResponseEntity.ok().build();

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(
            @PathVariable Long id
    ) {

        categoryService.deleteCategory(id);

        return ResponseEntity.ok().build();

    }
}