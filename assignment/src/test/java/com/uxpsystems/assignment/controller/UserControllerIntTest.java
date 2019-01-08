package com.uxpsystems.assignment.controller;

import com.uxpsystems.assignment.AssignmentApplication;
import com.uxpsystems.assignment.dao.UserDAO;
import com.uxpsystems.assignment.model.Status;
import com.uxpsystems.assignment.model.User;
import com.uxpsystems.assignment.service.UserService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the UserController REST controller.
 *
 * @see UserController
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssignmentApplication.class)
public class UserControllerIntTest {

    private static final String DEFAULT_USER_NAME = "AAAAAAAAAA";
    private static final String UPDATED_USER_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVATED;
    private static final Status UPDATED_STATUS = Status.DEACTIVATED;

    @Autowired
    private UserDAO userDao;

    @Autowired
    private UserService userService;


    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private EntityManager em;

    private MockMvc restUserMockMvc;

    private User user;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final UserController userController = new UserController(userService);
        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userController)
            .setCustomArgumentResolvers(pageableArgumentResolver).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static User createEntity(EntityManager em) {
    	User user = new User();
    	user.setUsername(DEFAULT_USER_NAME);
    	user.setPassword(DEFAULT_PASSWORD);
    	user.setStatus(DEFAULT_STATUS);
        return user;
    }

    @Before
    public void initTest() {
    	user = createEntity(em);
    }

    @Test
    @Transactional
    public void createUser() throws Exception {
        int databaseSizeBeforeCreate = userService.getAll().size();
        System.out.println("Size of databse before create is=>" + databaseSizeBeforeCreate);
        // Create the User
        restUserMockMvc.perform(post("/api/user")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(user)))
            .andExpect(status().isCreated());

        // Validate the user in the database
        List<User> userList = userService.getAll();
        assertThat(userList).hasSize(databaseSizeBeforeCreate + 1);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getUsername()).isEqualTo(DEFAULT_USER_NAME);
        assertThat(testUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);
        assertThat(testUser.getStatus()).isEqualTo(DEFAULT_STATUS);
    }
    
    @Test
    @Transactional
    public void updateUser() throws Exception {
    	// Initialize the database
    	userDao.saveAndFlush(user);

        int databaseSizeBeforeUpdate = userDao.findAll().size();
        System.out.println("Size of databse before update is=>" + databaseSizeBeforeUpdate);
        
        // Update the user1
        User updatedUser = userDao.findById(user.getId()).get();
        // Disconnect from session so that the updates on updatedUser1 are not directly saved in db
       // em.detach(updatedUser);
        updatedUser.setUsername(UPDATED_USER_NAME);
        updatedUser.setPassword(UPDATED_PASSWORD);
        updatedUser.setStatus(UPDATED_STATUS);

        //update user
        restUserMockMvc.perform(put("/api/user")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedUser)))
            .andExpect(status().isOk());

        // Validate the user in the database
        List<User> userList = userDao.findAll();
        assertThat(userList).hasSize(databaseSizeBeforeUpdate);
        User testUser = userList.get(userList.size() - 1);
        assertThat(testUser.getUsername()).isEqualTo(UPDATED_USER_NAME);
        assertThat(testUser.getPassword()).isEqualTo(UPDATED_PASSWORD);
        assertThat(testUser.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    public void deleteUser() throws Exception {
        // Initialize the database
    	userDao.saveAndFlush(user);

        int databaseSizeBeforeDelete = userDao.findAll().size();
        System.out.println("Size of databse before delete is=>" + databaseSizeBeforeDelete);
        
        // delete user
        restUserMockMvc.perform(delete("/api/user/{id}", user.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<User> user1List = userDao.findAll();
        assertThat(user1List).hasSize(databaseSizeBeforeDelete - 1);
    }
    
    
    @Test
    @Transactional
    public void getUsers() throws Exception {
        // Initialize the database
    	userDao.saveAndFlush(user);

        // Get all the userList
    	restUserMockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(user.getId().intValue())))
            .andExpect(jsonPath("$.[*].username").value(hasItem(DEFAULT_USER_NAME.toString())))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }
    
    @Test
    @Transactional
    public void getUser() throws Exception {
        // Initialize the database
    	userDao.saveAndFlush(user);

        // Get the user1
    	restUserMockMvc.perform(get("/api/user/{id}", user.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(user.getId().intValue()))
            .andExpect(jsonPath("$.username").value(DEFAULT_USER_NAME.toString()))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

}
