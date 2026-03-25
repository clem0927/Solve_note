package kr.solvenote.problemcategory.dto;

import lombok.Data;
import kr.solvenote.problemcategory.entity.ProblemCategory;

@Data
public class ProblemCategoryDto {

    private Long id;
    private String email;
    private String name;

    public static ProblemCategoryDto from(ProblemCategory category) {

        ProblemCategoryDto dto = new ProblemCategoryDto();

        dto.setId(category.getId());
        dto.setEmail(category.getAccount().getEmail());
        dto.setName(category.getName());

        return dto;
    }
}