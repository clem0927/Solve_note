package min.chat.child.problemcategory.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.problemcategory.dto.ProblemCategoryDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROBLEM_CATEGORY")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProblemCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private Account account;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    public static ProblemCategory from(ProblemCategoryDto dto, Account account) {
        return ProblemCategory.builder()
                .id(dto.getId())
                .account(account)
                .name(dto.getName())
                .build();
    }

    public void update(ProblemCategoryDto dto) {
        this.name = dto.getName();
    }
}