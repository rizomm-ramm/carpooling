package fr.rizomm.ramm.controller;

import fr.rizomm.ramm.form.SimpleJourneyForm;
import fr.rizomm.ramm.model.Journey;
import fr.rizomm.ramm.service.JourneyService;
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

import java.util.Date;

import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(MockitoJUnitRunner.class)
public class JourneyControllerTest {
    @Mock
    private JourneyService journeyService;

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
                .standaloneSetup(new JourneyController(journeyService))
                .setViewResolvers(viewResolver)
                .setHandlerExceptionResolvers()
                .build();
    }

    @After
    public void tearDown() throws Exception {
        reset(journeyService);
    }

    @Test
    public void testInitializeJourney_error() throws Exception {
        SimpleJourneyForm journeyForm = new SimpleJourneyForm();
        journeyForm.setDate(new Date());
        this.mockMvc.perform(post("/journey/initialize").flashAttr("journeyForm", journeyForm))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeHasFieldErrors("journeyForm", "departure.latitude"));
    }

    @Test
    public void testInitializeJourney_service_error() throws Exception {

        SimpleJourneyForm journeyForm = new SimpleJourneyForm();
        journeyForm.setDate(new Date());

        SimpleJourneyForm.Address arrival = journeyForm.getArrival();
        arrival.setAddress("Lille");
        arrival.setLatitude(50D);
        arrival.setLongitude(50D);
        arrival.setPrecision(20);

        SimpleJourneyForm.Address departure = journeyForm.getDeparture();
        departure.setAddress("Paris");
        departure.setLatitude(51D);
        departure.setLongitude(51D);
        departure.setPrecision(20);

        when(journeyService.createJourney(journeyForm, "akraxx")).thenThrow(new IllegalArgumentException());

        this.mockMvc.perform(post("/journey/initialize").flashAttr("journeyForm", journeyForm)
                    .principal(securityContext.getAuthentication()))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().hasErrors());
    }

    @Test
    public void testInitializeJourney() throws Exception {

        SimpleJourneyForm journeyForm = new SimpleJourneyForm();
        journeyForm.setDate(new Date());

        SimpleJourneyForm.Address arrival = journeyForm.getArrival();
        arrival.setAddress("Lille");
        arrival.setLatitude(50D);
        arrival.setLongitude(50D);
        arrival.setPrecision(20);

        SimpleJourneyForm.Address departure = journeyForm.getDeparture();
        departure.setAddress("Paris");
        departure.setLatitude(51D);
        departure.setLongitude(51D);
        departure.setPrecision(20);

        when(journeyService.createJourney(journeyForm, "akraxx")).thenReturn(Journey.builder().id(1L).build());

        this.mockMvc.perform(post("/journey/initialize").flashAttr("journeyForm", journeyForm)
                .principal(securityContext.getAuthentication()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/profile/journeys?type=driver&driverStoffOffid=1"));
    }

    @Test
    public void testCreateJourney() throws Exception {
        Journey journey = new Journey();
        when(journeyService.getOne(1L)).thenReturn(journey);

        this.mockMvc.perform(get("/journey/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("journey/item"))
                .andExpect(model().attribute("journey", journey));

        verify(journeyService, times(1)).getOne(1L);
    }
}