package min.chat.child.concept.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.category.entity.Category;
import min.chat.child.concept.dto.ConceptDto;

import java.time.LocalDateTime;

@Entity
@Table(name = "CONCEPT_RECORD")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Concept {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private Account account;

    @ManyToOne(optional = true)
    @JoinColumn(name = "category_id", nullable = true)
    private Category category;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(columnDefinition = "CLOB")
    private String answer;

    @Column(name = "is_favorite")
    private Boolean isFavorite;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isFavorite == null) {
            this.isFavorite = false;
        }
    }

    public static Concept from(ConceptDto dto, Account account, Category category) {
        return Concept.builder()
                .id(dto.getId())
                .account(account)
                .category(category)
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .isFavorite(dto.getIsFavorite())
                .build();
    }

    public void update(ConceptDto dto, Category category) {

        if(category != null){
            this.category = category;
        }

        if(dto.getQuestion() != null){
            this.question = dto.getQuestion();
        }

        if(dto.getAnswer() != null){
            this.answer = dto.getAnswer();
        }

        if(dto.getIsFavorite() != null){
            this.isFavorite = dto.getIsFavorite();
        }
    }
}