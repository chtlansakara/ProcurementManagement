package com.cht.procurementManagement.entities;

import jakarta.persistence.*;

@Entity
@Table(
        name="designation",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"title","grade"}, name = "idx_title_grade")}
)
public class Designation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "grade", nullable = false)
    private String grade;
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    //get-set methods

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
