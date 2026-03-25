package kr.solvenote.grade.service;

import lombok.RequiredArgsConstructor;
import kr.solvenote.grade.dto.GradeDto;
import kr.solvenote.grade.entity.Grade;
import kr.solvenote.grade.repository.GradeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository gradeRepository;

    @Transactional
    public void createGrade(GradeDto dto) {

        Grade grade = Grade.builder()
                .name(dto.getName())
                .maxConcept(dto.getMaxConcept())
                .maxProblem(dto.getMaxProblem())
                .maxChatPerDay(dto.getMaxChatPerDay())
                .build();

        gradeRepository.save(grade);
    }

}