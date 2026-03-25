package kr.solvenote.schedule.controller;

import lombok.RequiredArgsConstructor;
import kr.solvenote.schedule.dto.ScheduleDto;
import kr.solvenote.schedule.service.ScheduleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService scheduleService;

    @GetMapping
    public ResponseEntity<List<ScheduleDto>> getSchedules(@RequestParam String email){
        return ResponseEntity.ok(
                scheduleService.findSchedules(email)
        );
    }

    @PostMapping
    public ResponseEntity<Void> createSchedule(@RequestBody ScheduleDto dto){
        scheduleService.createSchedule(dto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSchedule(
            @PathVariable Long id,
            @RequestBody ScheduleDto dto
    ){
        scheduleService.updateSchedule(id, dto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id){
        scheduleService.deleteSchedule(id);
        return ResponseEntity.ok().build();
    }
}