package min.chat.child.guest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "GUEST_USAGE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GuestUsage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String ip;

    @Column(name = "guest_date", nullable = false)
    private LocalDate guestDate;

    @Column(nullable = false)
    private int chatCount;

}