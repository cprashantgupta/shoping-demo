package com.hypretail.vcart.web.rest;

import com.hypretail.vcart.domain.Authority;
import com.hypretail.vcart.domain.User;
import com.hypretail.vcart.repository.AuthorityRepository;
import com.hypretail.vcart.repository.UserRepository;
import com.hypretail.vcart.security.AuthoritiesConstants;
import com.hypretail.vcart.service.dto.UserDTO;
import com.hypretail.vcart.service.mapper.UserMapper;
import com.hypretail.vcart.web.rest.vm.ManagedUserVM;
import io.micronaut.cache.CacheManager;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import javax.inject.Inject;
import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration tests for the {@link UserResource} REST controller.
 */
@MicronautTest(transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserResourceIT {

    private static final String DEFAULT_LOGIN = "johndoe";
    private static final String UPDATED_LOGIN = "jhipster";

    private static final String DEFAULT_ID = "id1";

    private static final String DEFAULT_PASSWORD = "passjohndoe";
    private static final String UPDATED_PASSWORD = "passjhipster";

    private static final String DEFAULT_EMAIL = "johndoe@localhost";
    private static final String UPDATED_EMAIL = "jhipster@localhost";

    private static final String DEFAULT_FIRSTNAME = "john";
    private static final String UPDATED_FIRSTNAME = "jhipsterFirstName";

    private static final String DEFAULT_LASTNAME = "doe";
    private static final String UPDATED_LASTNAME = "jhipsterLastName";

    private static final String DEFAULT_IMAGEURL = "http://placehold.it/50x50";
    private static final String UPDATED_IMAGEURL = "http://placehold.it/40x40";

    private static final String DEFAULT_LANGKEY = "en";
    private static final String UPDATED_LANGKEY = "fr";

    @Inject
    private UserRepository userRepository;

    @Inject
    AuthorityRepository authorityRepository;

    @Inject
    private UserMapper userMapper;

    @Inject
    private CacheManager cacheManager;

    @Inject @Client("/")
    RxHttpClient client;

    private User user;

    public static User createEntity() {
        User user = new User();
        user.setLogin(DEFAULT_LOGIN + RandomStringUtils.randomAlphabetic(5));
        user.setId(DEFAULT_ID);
        user.setActivated(true);
        user.setEmail(RandomStringUtils.randomAlphabetic(5) + DEFAULT_EMAIL);
        user.setFirstName(DEFAULT_FIRSTNAME);
        user.setLastName(DEFAULT_LASTNAME);
        user.setImageUrl(DEFAULT_IMAGEURL);
        user.setLangKey(DEFAULT_LANGKEY);
        return user;
    }

    @BeforeAll
    public void initTest() {
        user = createEntity();
        user.setLogin(DEFAULT_LOGIN);
        user.setEmail(DEFAULT_EMAIL);
        userRepository.saveAndFlush(user);
        List<Authority> authorities = authorityRepository.findAll();
            if(authorities.isEmpty()) {
                // Set up expected authorities, ADMIN and USER
                Authority admin = new Authority();
                admin.setName(AuthoritiesConstants.ADMIN);
                authorityRepository.save(admin);
                Authority user = new Authority();
                user.setName(AuthoritiesConstants.USER);
                authorityRepository.save(user);
            }
    }

    @AfterAll
    public void cleanupTest() {
        userRepository.deleteAll();
    }

    @BeforeEach
    public void setup() {
        cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).invalidateAll();
        cacheManager.getCache(UserRepository.USERS_BY_EMAIL_CACHE).invalidateAll();
    }

    private void resetDefaultUser() {
        User reInsert = createEntity();
        reInsert.setLogin(DEFAULT_LOGIN);
        reInsert.setEmail(DEFAULT_EMAIL);
        userRepository.deleteById(user.getId());
        userRepository.flush();
        user = userRepository.saveAndFlush(reInsert);
    }

    @Test
    public void getAllUsers() throws Exception {
        // Get all the users
        List<User> users = client.retrieve(HttpRequest.GET("/api/users?sort=id,desc"), Argument.listOf(User.class)).blockingFirst();

        assertThat(users.get(0).getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(users.get(0).getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(users.get(0).getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(users.get(0).getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(users.get(0).getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(users.get(0).getLangKey()).isEqualTo(DEFAULT_LANGKEY);
    }

    @Test
    public void getUser() throws Exception {
        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(user.getLogin(), User.class)).isNotPresent();

        // Get the user
        User u = client.retrieve(HttpRequest.GET("/api/users/" + user.getLogin()), User.class).blockingFirst();

        assertThat(u.getLogin()).isEqualTo(user.getLogin());
        assertThat(u.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(u.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(u.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(u.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(u.getLangKey()).isEqualTo(DEFAULT_LANGKEY);

        assertThat(cacheManager.getCache(UserRepository.USERS_BY_LOGIN_CACHE).get(user.getLogin(), User.class)).isPresent();
    }

    @Test
    public void getNonExistingUser() throws Exception {
        HttpResponse<User> response = client.exchange(HttpRequest.GET("/api/users/unknown"), User.class).onErrorReturn(t -> (HttpResponse<User>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.getStatus().getCode()).isEqualTo(HttpStatus.NOT_FOUND.getCode());
    }

    @Test
    public void getAllAuthorities() throws Exception {

        HttpResponse<List<String>> response = client.exchange(HttpRequest.GET("/api/users/authorities"), Argument.listOf(String.class)).onErrorReturn(t -> (HttpResponse<List<String>>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.OK.getCode());
        assertThat(response.body()).contains(AuthoritiesConstants.USER);
        assertThat(response.body()).contains(AuthoritiesConstants.ADMIN);
    }

    @Test
    public void testUserEquals() throws Exception {
        User user1 = new User();
        user1.setId("id1");
        User user2 = new User();
        user2.setId("id1");
        assertThat(user1).isEqualTo(user2);
        user2.setId("id2");
        assertThat(user1).isNotEqualTo(user2);
        user1.setId(null);
        assertThat(user1).isNotEqualTo(user2);
    }

    @Test
    public void testUserDTOtoUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(DEFAULT_ID);
        userDTO.setLogin(DEFAULT_LOGIN);
        userDTO.setFirstName(DEFAULT_FIRSTNAME);
        userDTO.setLastName(DEFAULT_LASTNAME);
        userDTO.setEmail(DEFAULT_EMAIL);
        userDTO.setActivated(true);
        userDTO.setImageUrl(DEFAULT_IMAGEURL);
        userDTO.setLangKey(DEFAULT_LANGKEY);
        userDTO.setAuthorities(Collections.singleton(AuthoritiesConstants.USER));

        User user = userMapper.userDTOToUser(userDTO);
        assertThat(user.getId()).isEqualTo(DEFAULT_ID);
        assertThat(user.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(user.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(user.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(user.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(user.getActivated()).isEqualTo(true);
        assertThat(user.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(user.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        //assertThat(user.getCreatedDate()).isNotNull();
        //assertThat(user.getLastModifiedDate()).isNotNull();
        assertThat(user.getAuthorities()).extracting("name").containsExactly(AuthoritiesConstants.USER);
    }

    @Test
    public void testUserToUserDTO() {
        user.setId(DEFAULT_ID);
        user.setCreatedDate(Instant.now());
        user.setLastModifiedDate(Instant.now());
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.USER);
        authorities.add(authority);
        user.setAuthorities(authorities);

        UserDTO userDTO = userMapper.userToUserDTO(user);

        assertThat(userDTO.getId()).isEqualTo(DEFAULT_ID);
        assertThat(userDTO.getLogin()).isEqualTo(DEFAULT_LOGIN);
        assertThat(userDTO.getFirstName()).isEqualTo(DEFAULT_FIRSTNAME);
        assertThat(userDTO.getLastName()).isEqualTo(DEFAULT_LASTNAME);
        assertThat(userDTO.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(userDTO.isActivated()).isEqualTo(true);
        assertThat(userDTO.getImageUrl()).isEqualTo(DEFAULT_IMAGEURL);
        assertThat(userDTO.getLangKey()).isEqualTo(DEFAULT_LANGKEY);
        assertThat(userDTO.getCreatedDate()).isEqualTo(user.getCreatedDate());
        assertThat(userDTO.getLastModifiedDate()).isEqualTo(user.getLastModifiedDate());
        assertThat(userDTO.getAuthorities()).containsExactly(AuthoritiesConstants.USER);
        assertThat(userDTO.toString()).isNotNull();
    }

    @Test
    public void testAuthorityEquals() {
        Authority authorityA = new Authority();
        assertThat(authorityA).isEqualTo(authorityA);
        assertThat(authorityA).isNotEqualTo(null);
        assertThat(authorityA).isNotEqualTo(new Object());
        assertThat(authorityA.hashCode()).isEqualTo(0);
        assertThat(authorityA.toString()).isNotNull();

        Authority authorityB = new Authority();
        assertThat(authorityA).isEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.ADMIN);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityA.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isNotEqualTo(authorityB);

        authorityB.setName(AuthoritiesConstants.USER);
        assertThat(authorityA).isEqualTo(authorityB);
        assertThat(authorityA.hashCode()).isEqualTo(authorityB.hashCode());
    }
}
