package com.revature.PollingApp.model;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.NaturalId;

public class Role {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Enumerated(EnumType.STRING)
	@NaturalId
	@Column(length = 60)
	private RoleName role;
	
	
	public Role() {
		
	}


	public Role(RoleName role) {
		super();
		this.role = role;
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public RoleName getRole() {
		return role;
	}


	public void setRole(RoleName role) {
		this.role = role;
	}
	
	
	
}
