package kr.solvenote.schedule.service;

import lombok.RequiredArgsConstructor;
import kr.solvenote.account.entity.Account;
import kr.solvenote.account.repository.AccountRepository;
import kr.solvenote.schedule.dto.ScheduleCategoryDto;
import kr.solvenote.schedule.entity.ScheduleCategory;
import kr.solvenote.schedule.repository.ScheduleCategoryRepository;
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