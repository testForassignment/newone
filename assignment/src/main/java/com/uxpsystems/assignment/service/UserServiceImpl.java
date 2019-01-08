package com.uxpsystems.assignment.service;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uxpsystems.assignment.dao.UserDAO;
import com.uxpsystems.assignment.model.User;

@Service
public class UserServiceImpl implements UserService {
	
	@Autowired
	private UserDAO userDAO;

	@Override
	public User save(User entity) {
		return userDAO.save(entity);
	}

	@Override
	public Optional<User> findById(Serializable id) {
		return userDAO.findById((Long) id);
	}
	
	@Override
	public User getById(Serializable id) {
		return userDAO.getOne((Long) id);
	}

	@Override
	public List<User> getAll() {
		 return userDAO.findAll();
	}

	@Override
	public void delete(Serializable id) {
		userDAO.deleteById((Long) id);
	}

	@Override
	public User update(User user) {
		return userDAO.save(user);
	}
	
}