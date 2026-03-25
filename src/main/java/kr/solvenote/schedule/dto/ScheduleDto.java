package kr.solvenote.schedule.dto;

import lombok.Data;
import kr.solvenote.schedule.entity.Schedule;

import java.time.LocalDateTime;

@Data
public class ScheduleDto {

    private Long id;
    private String email;
    private Long categoryId;

    private String title;
    private String description;

    private LocalDateTime startAt;
    private LocalDateTime endAt;

    public static ScheduleDto from(Schedule schedule){
        ScheduleDto dto = new ScheduleDto();

        dto.id = schedule.getId();
        dto.email = schedule.getAccount().getEmail();
        dto.categoryId = schedule.getCategory() != null
                ? schedule.getCategory().getId()
                : null;

        dto.title = schedule.getTitle();
        dto.description = schedule.getDescription();
        dto.startAt = schedule.getStartAt();
        dto.endAt = schedule.getEndAt();

        return dto;
    }
}