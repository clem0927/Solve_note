package min.chat.child.schedule.service;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.account.repository.AccountRepository;
import min.chat.child.schedule.dto.ScheduleDto;
import min.chat.child.schedule.entity.Schedule;
import min.chat.child.schedule.entity.ScheduleCategory;
import min.chat.child.schedule.repository.ScheduleRepository;
import min.chat.child.schedule.repository.ScheduleCategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final AccountRepository accountRepository;
    private final ScheduleCategoryRepository categoryRepository;

    @Transactional
    public void createSchedule(ScheduleDto dto){

        Account account = accountRepository.findById(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("계정 없음"));

        ScheduleCategory category = null;

        if(dto.getCategoryId() != null){
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        }

        scheduleRepository.save(
                Schedule.from(dto, account, category)
        );
    }

    @Transactional(readOnly = true)
    public List<ScheduleDto> findSchedules(String email){

        return scheduleRepository.findByAccountEmail(email)
                .stream()
                .map(ScheduleDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateSchedule(Long id, ScheduleDto dto){

        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("일정 없음"));

        ScheduleCategory category = null;

        if(dto.getCategoryId() != null){
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        }

        schedule.update(dto, category);
    }

    @Transactional
    public void deleteSchedule(Long id){
        scheduleRepository.deleteById(id);
    }
}