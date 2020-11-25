package com.anhndn.assessment.apigateway.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "client_scope")
@IdClass(ClientScopeEntity.PrimaryKey.class)
public class ClientScopeEntity {

	@Id
	@Column(name = "client_id")
	private String clientId;

	@Id
	@Column(name = "api_id")
	private Integer apiId;

	public ClientScopeEntity(){}

	public String getClientId() {
		return clientId;
	}

	public void setClientId(String clientId) {
		this.clientId = clientId;
	}

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}

	public static final class PrimaryKey implements Serializable{

		private static final long serialVersionUID = 4590066832132670826L;

		public PrimaryKey() {
			super();
		}

        public PrimaryKey(String clientId, Integer apiId) {
            this.clientId = clientId;
            this.apiId = apiId;
        }
		@Column(name = "client_id")
		public String clientId;

		@Column(name = "api_id")
		public Integer apiId;
	}
	
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ClientScopeEntity [clientId=");
		builder.append(clientId);
		builder.append(", apiId=");
		builder.append(apiId);
		builder.append("]");
		return builder.toString();
	}
}
