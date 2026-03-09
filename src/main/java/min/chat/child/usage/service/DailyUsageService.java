package min.chat.child.usage.service;

import lombok.RequiredArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.account.repository.AccountRepository;
import min.chat.child.grade.entity.Grade;
import min.chat.child.grade.repository.GradeRepository;
import min.chat.child.usage.entity.DailyUsage;
import min.chat.child.usage.repository.DailyUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DailyUsageService {

    private final DailyUsageRepository dailyUsageRepository;
    private final AccountRepository accountRepository;
    private final GradeRepository gradeRepository;

    @Transactional
    public DailyUsage getOrCreate(String email) {

        LocalDate today = LocalDate.now();

        return dailyUsageRepository
                .findByEmailAndUsageDate(email, today)
                .orElseGet(() -> dailyUsageRepository.save(
                        DailyUsage.builder()
                                .email(email)
                                .usageDate(today)
                                .chatCount(0)
                                .build()
                ));
    }

    @Transactional
    public void increaseChat(String email) {

        DailyUsage usage = getOrCreate(email);

        Account account = accountRepository.findById(email)
                .orElseThrow();

        Grade grade = account.getGrade();   // 이미 연관관계 있음

        if (usage.getChatCount() >= grade.getMaxChatPerDay()) {
            throw new RuntimeException("오늘 채팅 횟수를 모두 사용했습니다.");
        }

        usage.setChatCount(usage.getChatCount() + 1);
    }
}