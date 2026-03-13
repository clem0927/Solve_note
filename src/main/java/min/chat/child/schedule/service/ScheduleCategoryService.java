package min.chat.child.schedule.service;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.account.repository.AccountRepository;
import min.chat.child.schedule.dto.ScheduleCategoryDto;
import min.chat.child.schedule.entity.ScheduleCategory;
import min.chat.child.schedule.repository.ScheduleCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleCategoryService {

    private final ScheduleCategoryRepository categoryRepository;
    private final AccountRepository accountRepository;

    public List<ScheduleCategoryDto> findCategories(String email){

        return categoryRepository.findByAccountEmail(email)
                .stream()
                .map(ScheduleCategoryDto::from)
                .collect(Collectors.toList());
    }

    public void createCategory(ScheduleCategoryDto dto){

        Account account = accountRepository.findById(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("계정 없음"));

        categoryRepository.save(
                ScheduleCategory.builder()
                        .account(account)
                        .name(dto.getName())
                        .color(dto.getColor())
                        .build()
        );
    }

    public void deleteCategory(Long id){
        categoryRepository.deleteById(id);
    }
}