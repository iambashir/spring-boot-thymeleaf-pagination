package com.bezkoder.spring.thymeleaf.pagination.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class BaseDto {
    private Long id;
    private String createdBy;
    private Date createdAt;
    private String updatedBy;
    private Date updateAt;
    private Integer activeStatus;
}
