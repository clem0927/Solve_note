package min.chat.child.schedule.controller;

import lombok.RequiredArgsConstructor;
import min.chat.child.schedule.dto.ScheduleCategoryDto;
import min.chat.child.schedule.service.ScheduleCategoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule-category")
@RequiredArgsConstructor
public class ScheduleCategoryController {

    private final ScheduleCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<ScheduleCategoryDto>> getCategories(
            @RequestParam String email
    ){
        return ResponseEntity.ok(
                categoryService.findCategories(email)
        );
    }

    @PostMapping
    public ResponseEntity<Void> createCategory(
            @RequestBody ScheduleCategoryDto dto
    ){
        categoryService.createCategory(dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.ok().build();
    }
}