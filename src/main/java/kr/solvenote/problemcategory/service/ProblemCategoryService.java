package kr.solvenote.problemcategory.service;

import lombok.RequiredArgsConstructor;
import kr.solvenote.account.entity.Account;
import kr.solvenote.account.repository.AccountRepository;
import kr.solvenote.problemcategory.dto.ProblemCategoryDto;
import kr.solvenote.problemcategory.entity.ProblemCategory;
import kr.solvenote.problemcategory.repository.ProblemCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProblemCategoryService {

    private final ProblemCategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void createCategory(ProblemCategoryDto dto) {

        Account account = accountRepository.findById(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("계정 없음"));

        categoryRepository.save(
                ProblemCategory.from(dto, account)
        );
    }

    @Transactional(readOnly = true)
    public List<ProblemCategoryDto> getCategories(String email) {

        return categoryRepository.findByAccountEmail(email)
                .stream()
                .map(ProblemCategoryDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateCategory(Long id, ProblemCategoryDto dto) {

        ProblemCategory category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        category.update(dto);
    }

    @Transactional
    public void deleteCategory(Long id) {

        categoryRepository.deleteById(id);

    }
}