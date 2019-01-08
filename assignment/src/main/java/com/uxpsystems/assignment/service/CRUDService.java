package com.uxpsystems.assignment.service;



import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.security.access.annotation.Secured;

import com.uxpsystems.assignment.model.User;

public interface CRUDService<U> {

	@Secured("ROLE_ADMIN")
	U save(U user);
	
	@Secured("ROLE_ADMIN")
	U update(U entity);
	
	User getById(Serializable id);
	
	Optional<User> findById(Serializable id);

	List<U> getAll();

	@Secured("ROLE_ADMIN")
	void delete(Serializable id);
}
