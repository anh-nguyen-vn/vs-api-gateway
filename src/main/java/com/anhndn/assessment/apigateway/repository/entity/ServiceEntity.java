package com.anhndn.assessment.apigateway.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "service")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ServiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "location")
    private String location;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_timestamp")
    private Date createdTimestamp;

    @Column(name = "last_updated_timestamp")
    private Date lastUpdatedTimestamp;

    @PrePersist
    public void preInsert() {
        this.isDeleted = false;
        Date createdDate = new Date();
        this.createdTimestamp = createdDate;
        this.lastUpdatedTimestamp = createdDate;
    }

    @PreUpdate
    public void preUpdate() {
        this.lastUpdatedTimestamp = new Date();
    }
}
