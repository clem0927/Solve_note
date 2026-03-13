package min.chat.child.schedule.dto;

import lombok.Data;
import min.chat.child.schedule.entity.ScheduleCategory;

@Data
public class ScheduleCategoryDto {

    private Long id;
    private String email;

    private String name;
    private String color;

    public static ScheduleCategoryDto from(ScheduleCategory category){

        ScheduleCategoryDto dto = new ScheduleCategoryDto();

        dto.id = category.getId();
        dto.email = category.getAccount().getEmail();
        dto.name = category.getName();
        dto.color = category.getColor();

        return dto;
    }
}