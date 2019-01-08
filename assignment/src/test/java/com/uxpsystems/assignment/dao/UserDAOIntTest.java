package com.uxpsystems.assignment.dao;

import java.util.Optional;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.uxpsystems.assignment.model.Status;
import com.uxpsystems.assignment.model.User;

import org.junit.Assert;

@RunWith(SpringRunner.class)
@DataJpaTest
public class UserDAOIntTest {

	private static final String FIRST_USER_NAME = "AAAAAAAAAA";
	private static final String SECOND_USER_NAME = "BBBBBBBBBB";

	private static final String FIRST_PASSWORD = "AAAAAAAAAA";
	private static final String SECOND_PASSWORD = "BBBBBBBBBB";

	private static final Status FIRST_STATUS = Status.ACTIVATED;
	private static final Status SECOND_STATUS = Status.DEACTIVATED;

	@Autowired
	private EntityManager em;

	@Autowired
	private UserDAO userDao;

	/**
	 * Create an entity1 for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static User createEntity1(EntityManager em) {
		User user = new User();
		user.setUsername(FIRST_USER_NAME);
		user.setPassword(FIRST_PASSWORD);
		user.setStatus(FIRST_STATUS);
		return user;
	}
	
	/**
	 * Create an entity2 for this test.
	 *
	 * This is a static method, as tests for other entities might also need it,
	 * if they test an entity which requires the current entity.
	 */
	public static User createEntity2(EntityManager em) {
		User user = new User();
		user.setUsername(SECOND_USER_NAME);
		user.setPassword(SECOND_PASSWORD);
		user.setStatus(SECOND_STATUS);
		return user;
	}

	@Test
	public void testSave() throws Exception {
		User user = createEntity1(em);
		User result = userDao.save(user);
		Assert.assertNotNull(userDao.findById(result.getId()));
	}

	@Test
	public void testDeleteById() throws Exception {
		User user = createEntity1(em);
		User result = userDao.save(user);
		userDao.deleteById(result.getId());

		// after the user is deleted, try to find it out
		Optional<User> opUser = userDao.findById(result.getId());
		User deletedUser = opUser.orElse(null);
		Assert.assertNull(deletedUser);
	}
	
	@Test
	public void testFindAll() throws Exception {
		User user1 = createEntity1(em);
		User user2 = createEntity2(em);
		userDao.save(user1);
		userDao.save(user2);
	
		// check there are two users existing
		Assert.assertEquals(2, userDao.findAll().size());
	}
	
	@Test
	public void testGetOne() throws Exception {
		User user1 = createEntity1(em);
		User user2 = createEntity2(em);
		userDao.save(user1);
		userDao.save(user2);
	
		User user = userDao.getOne(user2.getId());
		
		// check for the name of the second user
		Assert.assertEquals(SECOND_USER_NAME, user.getUsername());
	}
}
