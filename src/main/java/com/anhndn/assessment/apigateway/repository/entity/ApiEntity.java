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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "api")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

	@Column(name = "name")
	private String name;
	
	@Column(name = "http_method")
	private String httpMethod;

	@Column(name = "pattern")
	private String pattern;
	
	@Column(name = "is_required_access_token")
	private Boolean isRequiredAccessToken;
	
	@Column(name = "prefix")
	private String prefix;

    @Column(name = "service_id")
    private Long serviceId;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "created_timestamp")
    private Date createdTimestamp;

    @Column(name = "last_updated_timestamp")
    private Date lastUpdatedTimestamp;

    @ManyToOne
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    private ServiceEntity service;

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
