package fr.rizomm.ramm.controller;

import com.google.common.collect.ImmutableList;
import fr.rizomm.ramm.model.Notification;
import fr.rizomm.ramm.service.NotificationService;
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

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    private MockMvc mockMvc;

    private final SecurityContext securityContext = new SecurityContextImpl();

    @Before
    public void setUp() throws Exception {
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
                .standaloneSetup(new NotificationController(notificationService))
                .setViewResolvers(viewResolver)
                .setHandlerExceptionResolvers()
                .build();
    }

    @After
    public void tearDown() throws Exception {
        reset(notificationService);
    }

    @Test
    public void testUnreadNotifications() throws Exception {
        List<Notification> notifications = ImmutableList.of(new Notification(), new Notification());
        when(notificationService.getUnreadNotifications("akraxx")).thenReturn(
                notifications);

        this.mockMvc.perform(get("/notification/unread/count").principal(securityContext.getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(content().string("2"));

        verify(notificationService, times(1)).getUnreadNotifications("akraxx");
    }

    @Test
    public void testAllReadNotifications() throws Exception {
        this.mockMvc.perform(get("/notification/allread").principal(securityContext.getAuthentication()))
                .andExpect(status().isOk());

        verify(notificationService, times(1)).setAllReadNotifications("akraxx");
    }

    @Test
    public void testGetLastNotifications() throws Exception {
        List<Notification> notifications = ImmutableList.of(new Notification(), new Notification());
        when(notificationService.getLastNotifications("akraxx")).thenReturn(
                notifications);

        this.mockMvc.perform(get("/notification/").principal(securityContext.getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(view().name("notification"))
                .andExpect(model().attribute("notifications", is(notifications)));

        verify(notificationService, times(1)).getLastNotifications("akraxx");
    }
}