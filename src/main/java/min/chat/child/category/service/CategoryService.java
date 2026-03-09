package min.chat.child.category.service;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.account.repository.AccountRepository;
import min.chat.child.category.dto.CategoryDto;
import min.chat.child.category.entity.Category;
import min.chat.child.category.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public void createCategory(CategoryDto dto) {
        if (dto.getEmail() == null) {
            throw new RuntimeException("이메일이 필요합니다.");
        }

        Account account = accountRepository.findById(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("계정 없음"));

        categoryRepository.save(Category.from(dto, account));
    }

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll() {
        return categoryRepository.findAll()
                .stream()
                .map(CategoryDto::from)
                .toList();
    }

    @Transactional
    public void updateCategory(Long id, CategoryDto dto) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("카테고리 없음"));

        category.update(dto);
    }

    @Transactional
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }
}