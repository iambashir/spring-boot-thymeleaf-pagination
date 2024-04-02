package com.bezkoder.spring.thymeleaf.pagination.Dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TutorialDTO {

    private Integer id;
    private String title;
    private String description;
    private int level;
    private boolean published;

}
