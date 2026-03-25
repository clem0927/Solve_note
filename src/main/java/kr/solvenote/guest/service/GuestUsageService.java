package kr.solvenote.guest.service;

import lombok.RequiredArgsConstructor;
import kr.solvenote.guest.entity.GuestUsage;
import kr.solvenote.guest.repository.GuestUsageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class GuestUsageService {

    private final GuestUsageRepository guestUsageRepository;

    private static final int GUEST_CHAT_LIMIT = 5;

    @Transactional
    public GuestUsage getOrCreate(String ip) {

        LocalDate today = LocalDate.now();

        return guestUsageRepository
                .findByIpAndGuestDate(ip, today)
                .orElseGet(() -> guestUsageRepository.save(
                        GuestUsage.builder()
                                .ip(ip)
                                .guestDate(today)
                                .chatCount(0)
                                .build()
                ));
    }

    @Transactional
    public void increaseChat(String ip) {

        GuestUsage usage = getOrCreate(ip);

        //  제한 체크
        if (usage.getChatCount() >= GUEST_CHAT_LIMIT) {
            throw new RuntimeException("게스트는 하루 5회까지 채팅 가능합니다.");
        }

        usage.setChatCount(usage.getChatCount() + 1);
    }

}