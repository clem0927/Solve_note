package min.chat.child.concept.dto;

import lombok.Data;
import min.chat.child.concept.entity.Concept;

@Data
public class ConceptDto {

    private Long id;
    private String email;
    private Long categoryId;
    private String question;
    private String answer;
    private Boolean isFavorite;

    public static ConceptDto from(Concept concept) {
        ConceptDto dto = new ConceptDto();
        dto.id = concept.getId();
        dto.email = concept.getAccount().getEmail();
        // category가 null이면 null 처리
        dto.categoryId = concept.getCategory() != null ? concept.getCategory().getId() : null;
        dto.question = concept.getQuestion();
        dto.answer = concept.getAnswer();
        dto.isFavorite = concept.getIsFavorite();
        return dto;
    }
}