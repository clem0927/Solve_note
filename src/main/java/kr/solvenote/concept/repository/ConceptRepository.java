package kr.solvenote.concept.repository;

import kr.solvenote.concept.entity.Concept;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConceptRepository extends JpaRepository<Concept, Long> {

    // 이메일 기준 페이징 조회
    Page<Concept> findAllByAccountEmail(String email, Pageable pageable);

    // 이메일 + 검색어 페이징
    Page<Concept> findByAccountEmailAndQuestionContaining(String email, String keyword, Pageable pageable);

    // 이메일 + 카테고리 페이징
    Page<Concept> findByAccountEmailAndCategoryId(String email, Long categoryId, Pageable pageable);

    // 이메일 + 검색어 + 카테고리 페이징
    Page<Concept> findByAccountEmailAndCategoryIdAndQuestionContaining(String email, Long categoryId, String keyword, Pageable pageable);

    long countByAccountEmail(String email);
}