package kr.solvenote.usage.controller;

import lombok.RequiredArgsConstructor;
import kr.solvenote.usage.service.DailyUsageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usage")
@RequiredArgsConstructor
public class DailyUsageController {

    private final DailyUsageService dailyUsageService;

    @PostMapping("/chat")
    public ResponseEntity<Void> increaseChat(@RequestParam String email) {

        dailyUsageService.increaseChat(email);

        return ResponseEntity.ok().build();
    }

}