package com.bezkoder.spring.thymeleaf.pagination.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class TutorialDto extends BaseDto {
    private String title;
    private String description;
    private int level;
    private boolean published;
}
