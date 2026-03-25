package kr.solvenote.grade.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "GRADE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Column(nullable = false)
    private int maxConcept;

    @Column(nullable = false)
    private int maxProblem;

    @Column(nullable = false)
    private int maxChatPerDay;

}