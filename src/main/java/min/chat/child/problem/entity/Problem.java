package min.chat.child.problem.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import min.chat.child.account.entity.Account;
import min.chat.child.problem.dto.ProblemDto;
import min.chat.child.problemcategory.entity.ProblemCategory;

import java.time.LocalDateTime;

@Entity
@Table(name = "PROBLEM_RECORD")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Problem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "email", nullable = false)
    private Account account;

    @ManyToOne(optional = true)
    @JoinColumn(name = "pcategory_id", nullable = true)
    private ProblemCategory category;

    @Column(nullable = false, length = 500)
    private String question;

    @Column(length = 500)
    private String answer;

    @Column(columnDefinition = "CLOB")
    private String solution;

    @Column(name = "is_favorite")
    private Boolean isFavorite;

    @Column(length = 20)
    private String difficulty;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.isFavorite == null) {
            this.isFavorite = false;
        }
    }

    public static Problem from(ProblemDto dto, Account account, ProblemCategory category) {
        return Problem.builder()
                .id(dto.getId())
                .account(account)
                .category(category)
                .question(dto.getQuestion())
                .answer(dto.getAnswer())
                .solution(dto.getSolution())
                .difficulty(dto.getDifficulty())
                .isFavorite(dto.getIsFavorite())
                .build();
    }

    public void update(ProblemDto dto, ProblemCategory category) {


        this.category = category;


        if(dto.getQuestion() != null){
            this.question = dto.getQuestion();
        }

        if(dto.getAnswer() != null){
            this.answer = dto.getAnswer();
        }

        if(dto.getSolution() != null){
            this.solution = dto.getSolution();
        }

        if(dto.getDifficulty() != null){
            this.difficulty = dto.getDifficulty();
        }

        if(dto.getIsFavorite() != null){
            this.isFavorite = dto.getIsFavorite();
        }
    }
    public void changeProblemCategory(ProblemCategory category){
        this.category = category;
    }
}