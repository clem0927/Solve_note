package kr.solvenote.problemcategory.repository;

import kr.solvenote.problemcategory.entity.ProblemCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProblemCategoryRepository extends JpaRepository<ProblemCategory, Long> {

    List<ProblemCategory> findByAccountEmail(String email);

}