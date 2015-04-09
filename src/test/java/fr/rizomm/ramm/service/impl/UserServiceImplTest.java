package fr.rizomm.ramm.service.impl;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.common.collect.ImmutableList;

import fr.rizomm.ramm.model.User;
import fr.rizomm.ramm.repositories.UserRepository;
import fr.rizomm.ramm.service.UserService;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

	private UserService userService ;

	@Mock
	private UserRepository userRepository;
	
	private User user1 = User.builder()
			.username("user1")
			.password("123")
			.build();
	
	private User user2 = User.builder()
			.username("user2")
			.password("654321")
			.build();

    @Before
    public void setUp() throws Exception {
        userService = new UserServiceImpl(userRepository);
    }

    @After
    public void tearDown() throws Exception {
        reset(userRepository);
    }

	@Test
    public void testFindAll() {
        // Quand on appelle la méthode findAll du repository, on retourne [user1, user1]
        when(userRepository.findAll()).thenReturn(ImmutableList.of(user1, user2));

        List<User> users = userService.findAll();

        // Vérification du retour du service
        assertThat(users, hasItems(user1, user2));

        // Vérification que le repository a bien été appelé
        verify(userRepository, times(1)).findAll();
    }

	@Test
    public void testGetOne() {
        // Quand on appelle la méthode getOne("user1") du repository, on retourne user1
        when(userRepository.getOne("user1")).thenReturn(user1);

        User foundUser = userService.getOne("user1");

        // Vérification du retour du service
        assertThat(foundUser, equalTo(user1));

        // Vérification que le repository a bien été appelé
        verify(userRepository, times(1)).getOne("user1");
    }

}
