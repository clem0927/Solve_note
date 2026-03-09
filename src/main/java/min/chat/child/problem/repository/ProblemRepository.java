package min.chat.child.problem.repository;

import min.chat.child.problem.entity.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    Page<Problem> findAllByAccountEmail(String email, Pageable pageable);

    Page<Problem> findByAccountEmailAndQuestionContaining(String email, String keyword, Pageable pageable);

    Page<Problem> findByAccountEmailAndCategoryId(String email, Long categoryId, Pageable pageable);

    Page<Problem> findByAccountEmailAndCategoryIdAndQuestionContaining(
            String email,
            Long categoryId,
            String keyword,
            Pageable pageable
    );
    long countByAccountEmail(String email);
}