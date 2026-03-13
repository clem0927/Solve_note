package min.chat.child.schedule.entity;

import jakarta.persistence.*;
import lombok.*;
import min.chat.child.account.entity.Account;
import min.chat.child.schedule.dto.ScheduleDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "SCHEDULE")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private Account account;

    @ManyToOne(optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private ScheduleCategory category;

    @Column(length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(name = "start_at")
    private LocalDateTime startAt;

    @Column(name = "end_at")
    private LocalDateTime endAt;

    public static Schedule from(ScheduleDto dto, Account account, ScheduleCategory category){
        return Schedule.builder()
                .id(dto.getId())
                .account(account)
                .category(category)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .startAt(dto.getStartAt())
                .endAt(dto.getEndAt())
                .build();
    }

    public void update(ScheduleDto dto, ScheduleCategory category){

        if(category != null){
            this.category = category;
        }

        if(dto.getTitle() != null){
            this.title = dto.getTitle();
        }

        if(dto.getDescription() != null){
            this.description = dto.getDescription();
        }

        if(dto.getStartAt() != null){
            this.startAt = dto.getStartAt();
        }

        if(dto.getEndAt() != null){
            this.endAt = dto.getEndAt();
        }
    }
}