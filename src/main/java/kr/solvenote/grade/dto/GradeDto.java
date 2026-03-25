package kr.solvenote.grade.dto;

import lombok.Data;
import kr.solvenote.grade.entity.Grade;

@Data
public class GradeDto {

    private Long id;
    private String name;
    private int maxConcept;
    private int maxProblem;
    private int maxChatPerDay;

    public static GradeDto from(Grade grade) {

        GradeDto dto = new GradeDto();

        dto.id = grade.getId();
        dto.name = grade.getName();
        dto.maxConcept = grade.getMaxConcept();
        dto.maxProblem = grade.getMaxProblem();
        dto.maxChatPerDay = grade.getMaxChatPerDay();

        return dto;
    }

}