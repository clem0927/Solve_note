package min.chat.child.schedule.entity;

import jakarta.persistence.*;
import lombok.*;
import min.chat.child.account.entity.Account;

@Entity
@Table(name = "SCHEDULE_CATEGORY")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="email", nullable=false)
    private Account account;

    @Column(nullable=false, length=50)
    private String name;

    @Column(length=20)
    private String color;
}