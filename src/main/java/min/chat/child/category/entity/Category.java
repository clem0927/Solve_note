package min.chat.child.category.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.category.dto.CategoryDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "CATEGORY")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Category {

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

    public static Category from(CategoryDto dto, Account account) {
        return Category.builder()
                .account(account)
                .name(dto.getName())
                .build(); // id 제거
    }

    public void update(CategoryDto dto) {
        this.name = dto.getName();
    }
}