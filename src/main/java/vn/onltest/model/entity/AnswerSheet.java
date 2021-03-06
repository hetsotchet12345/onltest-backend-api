package vn.onltest.model.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity(name = "AnswerSheets")
@Getter
@Setter
@NoArgsConstructor
public class AnswerSheet implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "text")
    private String chosenAnswer;

    private long optionId;

    private Date chosenTime;
    private double grade;

    @ManyToOne
    private TestingResult testingResult;

    @ManyToOne
    private Question question;
}
