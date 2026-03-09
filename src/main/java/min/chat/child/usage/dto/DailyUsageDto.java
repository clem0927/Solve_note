package min.chat.child.usage.dto;

import lombok.Data;
import min.chat.child.usage.entity.DailyUsage;

import java.time.LocalDate;

@Data
public class DailyUsageDto {

    private Long id;
    private String email;
    private LocalDate usageDate;
    private int chatCount;

    public static DailyUsageDto from(DailyUsage usage) {

        DailyUsageDto dto = new DailyUsageDto();

        dto.id = usage.getId();
        dto.email = usage.getEmail();
        dto.usageDate = usage.getUsageDate();
        dto.chatCount = usage.getChatCount();

        return dto;
    }

}