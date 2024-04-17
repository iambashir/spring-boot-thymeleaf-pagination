package com.bezkoder.spring.thymeleaf.pagination.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tutorials")
public class Tutorial extends BaseEntity{

  @Column(length = 128, nullable = false)
  private String title;

  @Column(length = 256)
  private String description;

  @Column(nullable = false)
  private int level;

  @Column
  private boolean published;

}
