package kr.solvenote.problem.dto;

import lombok.Data;
import kr.solvenote.problem.entity.Problem;

@Data
public class ProblemDto {

    private Long id;
    private String email;
    private Long categoryId;

    private String question;
    private String answer;
    private String solution;

    private String difficulty;
    private Boolean isFavorite;

    public static ProblemDto from(Problem problem) {
        ProblemDto dto = new ProblemDto();

        dto.id = problem.getId();
        dto.email = problem.getAccount().getEmail();
        dto.categoryId = problem.getCategory() != null ? problem.getCategory().getId() : null;

        dto.question = problem.getQuestion();
        dto.answer = problem.getAnswer();
        dto.solution = problem.getSolution();
        dto.difficulty = problem.getDifficulty();
        dto.isFavorite = problem.getIsFavorite();

        return dto;
    }
}