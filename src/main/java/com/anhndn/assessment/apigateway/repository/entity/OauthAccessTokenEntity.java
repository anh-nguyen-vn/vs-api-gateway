package com.anhndn.assessment.apigateway.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "oauth_access_token")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class OauthAccessTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "token_value")
    private String tokenValue;

    @Column(name = "client_id")
    private String clientId;

    @Column(name = "scope")
    private String scope;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_type_id")
    private Integer userTypeId;

    @Column(name = "username")
    private String username;

    @Column(name = "refresh_token")
    private String refreshToken;

    @Column(name = "authorized_grant_types")
    private String authorizedGrantTypes;

    @Column(name = "created_timestamp")
    private Date createdTimestamp;

    @ManyToOne
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    private OAuthClientDetailsEntity oAuthClientDetailsEntity;

    @PrePersist
    public void preInsert() {
        this.createdTimestamp = new Date();
    }
}
