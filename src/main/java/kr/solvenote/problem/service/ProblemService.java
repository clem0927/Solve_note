package kr.solvenote.problem.service;

import lombok.RequiredArgsConstructor;
import kr.solvenote.account.entity.Account;
import kr.solvenote.account.repository.AccountRepository;
import kr.solvenote.problem.dto.ProblemDto;
import kr.solvenote.problem.entity.Problem;
import kr.solvenote.problem.repository.ProblemRepository;
import kr.solvenote.problemcategory.entity.ProblemCategory;
import kr.solvenote.problemcategory.repository.ProblemCategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProblemService {

    private final ProblemRepository problemRepository;
    private final AccountRepository accountRepository;
    private final ProblemCategoryRepository categoryRepository;

    @Transactional
    public void createProblem(ProblemDto dto) {

        Account account = accountRepository.findById(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("계정 없음"));

        // 현재 문제 개수
        long problemCount = problemRepository.countByAccountEmail(dto.getEmail());

        // 사용자 등급
        int maxProblem = account.getGrade().getMaxProblem();

        if (problemCount >= maxProblem) {
            throw new RuntimeException("문제 저장 한도를 초과했습니다.");
        }

        ProblemCategory category = null;

        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        }

        problemRepository.save(
                Problem.from(dto, account, category)
        );
    }

    @Transactional(readOnly = true)
    public Page<ProblemDto> findProblems(String email, String keyword, Long categoryId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        Page<Problem> problems;

        boolean hasKeyword = keyword != null && !keyword.isEmpty();
        boolean hasCategory = categoryId != null;

        if (hasKeyword && hasCategory) {
            problems = problemRepository.findByAccountEmailAndCategoryIdAndQuestionContaining(email, categoryId, keyword, pageable);
        } else if (hasKeyword) {
            problems = problemRepository.findByAccountEmailAndQuestionContaining(email, keyword, pageable);
        } else if (hasCategory) {
            problems = problemRepository.findByAccountEmailAndCategoryId(email, categoryId, pageable);
        } else {
            problems = problemRepository.findAllByAccountEmail(email, pageable);
        }

        return problems.map(ProblemDto::from);
    }

    @Transactional
    public void updateProblem(Long id, ProblemDto dto) {

        Problem problem = problemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("문제 없음"));

        ProblemCategory category = null;

        if (dto.getCategoryId() != null) {
            category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        }

        problem.changeProblemCategory(category);
    }

    @Transactional
    public void deleteProblem(Long id) {
        problemRepository.deleteById(id);
    }
    @Transactional
    public void updateProblemCategory(Long problemId, Long categoryId) {

        Problem problem = problemRepository.findById(problemId)
                .orElseThrow(() -> new RuntimeException("문제 없음"));

        ProblemCategory category = null;

        if (categoryId != null) {
            category = categoryRepository.findById(categoryId)
                    .orElseThrow(() -> new RuntimeException("카테고리 없음"));
        }

        problem.update(problemToDto(problem), category);
    }

    private ProblemDto problemToDto(Problem problem) {

        ProblemDto dto = new ProblemDto();

        dto.setId(problem.getId());
        dto.setEmail(problem.getAccount().getEmail());
        dto.setCategoryId(problem.getCategory() != null ? problem.getCategory().getId() : null);
        dto.setQuestion(problem.getQuestion());
        dto.setAnswer(problem.getAnswer());
        dto.setSolution(problem.getSolution());
        dto.setDifficulty(problem.getDifficulty());
        dto.setIsFavorite(problem.getIsFavorite());

        return dto;
    }
}