package min.chat.child.problemcategory.repository;

import min.chat.child.problemcategory.entity.ProblemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemCategoryRepository extends JpaRepository<ProblemCategory, Long> {

    List<ProblemCategory> findByAccountEmail(String email);

}