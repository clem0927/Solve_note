package min.chat.child.problemcategory.service;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.account.repository.AccountRepository;
import min.chat.child.problemcategory.dto.ProblemCategoryDto;
import min.chat.child.problemcategory.entity.ProblemCategory;
import min.chat.child.problemcategory.repository.ProblemCategoryRepository;
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