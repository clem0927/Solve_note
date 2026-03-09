package min.chat.child.concept.controller;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import min.chat.child.concept.dto.ConceptDto;
import min.chat.child.concept.service.ConceptService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/concept")
@RequiredArgsConstructor
public class ConceptController {

    private final ConceptService conceptService;

    // 페이징 + 검색 API
    @GetMapping
    public ResponseEntity<Page<ConceptDto>> getConcepts(
            @RequestParam String email,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<ConceptDto> concepts = conceptService.findConcepts(email, keyword, categoryId, page, size);
        return ResponseEntity.ok(concepts);
    }


    @PostMapping
    public ResponseEntity<Void> createConcept(@RequestBody ConceptDto dto) {
        conceptService.createConcept(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateConcept(@PathVariable Long id,
                                              @RequestBody ConceptDto dto) {
        conceptService.updateConcept(id, dto);
        return ResponseEntity.ok().build();
    }
    // ================= 드래그앤드롭: 개념 카테고리 변경 =================
    @PutMapping("/{id}/category")
    public ResponseEntity<Void> updateConceptCategory(
            @PathVariable Long id,
            @RequestBody CategoryUpdateRequest request
    ) {
        conceptService.updateConceptCategory(id, request.getCategoryId());
        return ResponseEntity.ok().build();
    }

    @Data
    static class CategoryUpdateRequest {
        private Long categoryId; // null이면 전체(All)
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteConcept(@PathVariable Long id) {
        conceptService.deleteConcept(id);
        return ResponseEntity.ok().build();
    }
}