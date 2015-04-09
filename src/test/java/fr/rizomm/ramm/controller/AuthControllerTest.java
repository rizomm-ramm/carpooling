package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.junit.Assert.*;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by Maximilien on 09/04/2015.
 */
@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {
    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(userService))
                .setViewResolvers(viewResolver)
                .build();
    }

    @After
    public void tearDown() throws Exception {
        reset(userService);
    }

    @Test
    public void testLogin_noParam() throws Exception {
        this.mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("j_username", ""))
                .andExpect(model().attribute("j_password", ""));
    }

    @Test
    public void testLogin_error() throws Exception {
        this.mockMvc.perform(get("/login").param("error", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("error", AuthController.INVALID_CREDENTIALS))
                .andExpect(model().attribute("j_username", ""))
                .andExpect(model().attribute("j_password", ""));
    }

    @Test
    public void testLogin_logout() throws Exception {
        this.mockMvc.perform(get("/login").param("logout", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("msg", AuthController.LOGOUT))
                .andExpect(model().attribute("j_username", ""))
                .andExpect(model().attribute("j_password", ""));
    }

    @Test
    public void testRegister_ok() throws Exception {
        String username = "username";
        String password = "password";
        this.mockMvc.perform(post("/register").param("j_username", username).param("j_password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("j_username", username))
                .andExpect(flash().attribute("j_password", password))
                .andExpect(flash().attribute("notifications", hasItem(AuthController.REGISTRATION_OK + username)));
    }

    @Test
    public void testRegister_existing_username() throws Exception {
        String username = "username";
        String password = "password";

        String errorMessage = "Username existant";
        when(userService.register(username, password)).thenThrow(new IllegalArgumentException(errorMessage));

        this.mockMvc.perform(post("/register").param("j_username", username).param("j_password", password))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("errors", hasItem(AuthController.REGISTRATION_ERROR + errorMessage)));
    }
}