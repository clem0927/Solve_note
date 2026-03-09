package min.chat.child.account.entity;

import jakarta.persistence.*;
import lombok.*;
import min.chat.child.grade.entity.Grade;

import java.time.LocalDateTime;

@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @Column(nullable = false, length = 255)
    private String email;

    // OAuth 때문에 nullable
    @Column(length = 255)
    private String password;

    @Column(nullable = false, length = 50)
    private String nickname;

    @Column(nullable = false, length = 20)
    private String role = "USER";

    // OAuth provider
    @Column(length = 20)
    private String provider; // local / google

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "grade_id")
    private Grade grade;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public void changePassword(String newPw) {
        this.password = newPw;
    }
}