package kr.solvenote.guest.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import kr.solvenote.guest.service.GuestUsageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guest")
@RequiredArgsConstructor
public class GuestUsageController {

    private final GuestUsageService guestUsageService;

    @PostMapping("/chat")
    public ResponseEntity<Void> increaseGuestChat(HttpServletRequest request) {

        String ip = request.getHeader("X-Forwarded-For");

        if (ip != null && !ip.isEmpty()) {
            ip = ip.split(",")[0];
        } else {
            ip = request.getRemoteAddr();
        }

        guestUsageService.increaseChat(ip);

        return ResponseEntity.ok().build();
    }

}