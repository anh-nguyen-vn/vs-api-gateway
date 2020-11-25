package com.anhndn.assessment.apigateway.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

@Entity
@Table(name = "oauth_client_details")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class OAuthClientDetailsEntity {

	@Id
	@Column(name = "client_id")
	private String clientId;

	@Column(name = "client_secret")
	private String clientSecret;

	@Column(name = "client_name")
	private String clientName;

	@Column(name = "authorized_grant_types")
	private String authorizedGrantTypes;

	@Column(name = "access_token_validity")
	private Integer accessTokenValidity;

	@Column(name = "refresh_token_validity")
	private Integer refreshTokenValidity;

	@Column(name = "is_deleted")
	private Boolean isDeleted;

	@Column(name = "created_timestamp")
	private Date createdTimestamp;

	@Column(name = "last_updated_timestamp")
	private Date lastUpdatedTimestamp;

	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "client_scope",
			joinColumns = @JoinColumn(name = "client_id", referencedColumnName = "client_id"),
			inverseJoinColumns = @JoinColumn(name = "api_id", referencedColumnName = "id"))
	private Set<ApiEntity> clientScopes;

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
