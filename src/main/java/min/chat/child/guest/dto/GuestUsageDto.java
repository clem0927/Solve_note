package min.chat.child.guest.dto;

import lombok.Data;
import min.chat.child.guest.entity.GuestUsage;

import java.time.LocalDate;

@Data
public class GuestUsageDto {

    private Long id;
    private String ip;
    private LocalDate guestDate;
    private int chatCount;

    public static GuestUsageDto from(GuestUsage usage) {

        GuestUsageDto dto = new GuestUsageDto();

        dto.id = usage.getId();
        dto.ip = usage.getIp();
        dto.guestDate = usage.getGuestDate();
        dto.chatCount = usage.getChatCount();

        return dto;
    }

}