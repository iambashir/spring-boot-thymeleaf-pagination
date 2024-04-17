package com.bezkoder.spring.thymeleaf.pagination.entity;

import com.bezkoder.spring.thymeleaf.pagination.enums.ActiveStatus;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;


@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(updatable = false)
    private String createdBy;

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    private String updatedBy;

    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;
    private Integer activeStatus;

    @PrePersist
    public void setPreInsertData() {
        this.createdAt = new Date();
        if (this.activeStatus == null) {
            this.activeStatus = ActiveStatus.ACTIVE.getValue();
        }
    }

    @PreUpdate
    public void setPreUpdateData() {
        this.updateAt = new Date();
    }

}
