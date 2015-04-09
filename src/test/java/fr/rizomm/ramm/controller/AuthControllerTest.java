package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.util.NestedServletException;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class AuthControllerTest {
    @Mock
    private UserService userService;

    private MockMvc mockMvc;

    private final SecurityContext securityContext = new SecurityContextImpl();

    private User user = User.builder().username("akraxx").build();
    @Before
    public void setup() {
        final Authentication authentication = new TestingAuthenticationToken("akraxx", "password");
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");

        // Process mock annotations
        MockitoAnnotations.initMocks(this);

        // Setup Spring test in standalone mode
        this.mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(userService))
                .setViewResolvers(viewResolver)
                .setHandlerExceptionResolvers()
                .build();

        when(userService.getOne("akraxx")).thenReturn(user);
    }

    @After
    public void tearDown() throws Exception {
        reset(userService);
        SecurityContextHolder.clearContext();
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

    @Test(expected = NestedServletException.class)
    public void testProfileInformations_not_connected() throws Exception {
        this.mockMvc.perform(get("/profile"));
    }

    @Test
    public void testProfileInformations() throws Exception {
        this.mockMvc.perform(get("/profile").principal(securityContext.getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/informations"))
                .andExpect(model().attribute("user", user));
    }

    @Test(expected = NestedServletException.class)
    public void testProfileJourneys_not_connected() throws Exception {
        this.mockMvc.perform(get("/profile/journeys"));
    }

    @Test
    public void testProfileJourneys() throws Exception {
        this.mockMvc.perform(get("/profile/journeys").principal(securityContext.getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/journeys"))
                .andExpect(model().attribute("user", user))
                .andExpect(model().attribute("bookSeatForm", is(notNullValue())));
    }

    @Test(expected = NestedServletException.class)
    public void testProfileMessages_not_connected() throws Exception {
        this.mockMvc.perform(get("/profile/messages"));
    }

    @Test
    public void testProfileMessages() throws Exception {
        this.mockMvc.perform(get("/profile/messages").principal(securityContext.getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(view().name("profile/messages"))
                .andExpect(model().attribute("user", user));
    }
}