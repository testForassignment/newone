package com.uxpsystems.assignment.service;

import java.io.Serializable;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.uxpsystems.assignment.dao.UserDAO;
import com.uxpsystems.assignment.model.Status;
import com.uxpsystems.assignment.model.User;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class UserServiceImplIntTest {
	private static final String FIRST_USER_NAME = "AAAAAAAAAA";
	private static final String SECOND_USER_NAME = "BBBBBBBBBB";

	private static final String FIRST_PASSWORD = "AAAAAAAAAA";
	private static final String SECOND_PASSWORD = "BBBBBBBBBB";

	private static final Status FIRST_STATUS = Status.ACTIVATED;
	private static final Status SECOND_STATUS = Status.DEACTIVATED;
 
    @TestConfiguration
    static class UserServiceImplTestContextConfiguration {
        @Bean
        public UserService userService() {
            return new UserServiceImpl();
        }
    }
 
    @Autowired
    private UserService userService;
 
    @MockBean
    private UserDAO userDao;
 
    @Before
    public void setUp() {
        User user = new User();
        user.setId(1L);
        user.setUsername(FIRST_USER_NAME);
		user.setPassword(FIRST_PASSWORD);
		user.setStatus(FIRST_STATUS);
		
        Mockito.when(userDao.getOne(user.getId()))
          .thenReturn(user);
        
        User user1 = new User();
        user1.setId(1L);
        user1.setUsername(SECOND_USER_NAME);
		user1.setPassword(SECOND_PASSWORD);
		user1.setStatus(SECOND_STATUS);
        
        Mockito.when(userDao.save(user1))
        .thenReturn(user1);
    }
    
    @Test
    public void testGetById() {
        Serializable id = 1L;
		User found = userService.getById(id);
      
         assertThat(found.getUsername())
          .isEqualTo(FIRST_USER_NAME);
     }
    
    @Test
    public void testUpdate() {
    	Serializable id = 1L;
    	User existingUser = userService.getById(id);
    	existingUser.setUsername(SECOND_USER_NAME);
    	existingUser.setPassword(SECOND_PASSWORD);
    	existingUser.setStatus(SECOND_STATUS);

		User updatedUser = userService.update(existingUser);
        assertThat(updatedUser.getUsername())
          .isEqualTo(SECOND_USER_NAME);
        assertThat(updatedUser.getStatus())
        .isEqualTo(SECOND_STATUS);
     }
}