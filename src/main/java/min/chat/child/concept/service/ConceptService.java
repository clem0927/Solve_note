package min.chat.child.concept.service;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.account.repository.AccountRepository;
import min.chat.child.category.entity.Category;
import min.chat.child.category.repository.CategoryRepository;
import min.chat.child.concept.dto.ConceptDto;
import min.chat.child.concept.entity.Concept;
import min.chat.child.concept.repository.ConceptRepository;
import min.chat.child.grade.entity.Grade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConceptService {

    private final ConceptRepository conceptRepository;
    private final AccountRepository accountRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public void createConcept(ConceptDto dto) {

        Account account = accountRepository.findById(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("계정 없음"));

        Grade grade = account.getGrade();

        long conceptCount = conceptRepository.countByAccountEmail(dto.getEmail());

        if (conceptCount >= grade.getMaxConcept()) {
            throw new RuntimeException("개념 저장 한도를 초과했습니다.");
        }

        Category category = null;

        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        }

        conceptRepository.save(
                Concept.from(dto, account, category)
        );
    }


    @Transactional(readOnly = true)
    public Page<ConceptDto> findConcepts(String email, String keyword, Long categoryId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Concept> concepts;

        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasCategory = categoryId != null;

        if (hasKeyword && hasCategory) {
            concepts = conceptRepository.findByAccountEmailAndCategoryIdAndQuestionContaining(email, categoryId, keyword, pageable);
        } else if (hasKeyword) {
            concepts = conceptRepository.findByAccountEmailAndQuestionContaining(email, keyword, pageable);
        } else if (hasCategory) {
            concepts = conceptRepository.findByAccountEmailAndCategoryId(email, categoryId, pageable);
        } else {
            concepts = conceptRepository.findAllByAccountEmail(email, pageable);
        }

        return concepts.map(ConceptDto::from);
    }

    @Transactional
    public void updateConcept(Long id, ConceptDto dto) {

        Concept concept = conceptRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("개념 없음"));

        Category category = null;
        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        }
        concept.update(dto, category);
    }

    // ================= 드래그앤드롭: 카테고리 업데이트 =================
    @Transactional
    public void updateConceptCategory(Long conceptId, Long categoryId) {
        Concept concept = conceptRepository.findById(conceptId)
                .orElseThrow(() -> new RuntimeException("개념 없음"));

        Category category = null;
        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        }

        concept.changeCategory(category);
    }

    // DTO 생성: 기존 Concept 값 유지
    private ConceptDto conceptToDto(Concept concept) {
        ConceptDto dto = new ConceptDto();
        dto.setId(concept.getId());
        dto.setEmail(concept.getAccount().getEmail());
        dto.setCategoryId(concept.getCategory() != null ? concept.getCategory().getId() : null);
        dto.setQuestion(concept.getQuestion());
        dto.setAnswer(concept.getAnswer());
        dto.setIsFavorite(concept.getIsFavorite());
        return dto;
    }


    @Transactional
    public void deleteConcept(Long id) {
        conceptRepository.deleteById(id);
    }
}