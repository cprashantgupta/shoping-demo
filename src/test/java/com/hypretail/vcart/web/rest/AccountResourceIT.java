package com.hypretail.vcart.web.rest;

import com.hypretail.vcart.VcartApp;
import com.hypretail.vcart.config.Constants;
import com.hypretail.vcart.domain.Authority;
import com.hypretail.vcart.domain.User;
import com.hypretail.vcart.repository.AuthorityRepository;
import com.hypretail.vcart.repository.UserRepository;
import com.hypretail.vcart.security.AuthoritiesConstants;
import com.hypretail.vcart.service.MailService;
import com.hypretail.vcart.service.UserService;
import com.hypretail.vcart.service.dto.PasswordChangeDTO;
import com.hypretail.vcart.service.dto.UserDTO;
import com.hypretail.vcart.web.rest.vm.ManagedUserVM;
import io.micronaut.context.annotation.Property;
import io.micronaut.core.type.Argument;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.HttpStatus;
import io.micronaut.http.MediaType;
import io.micronaut.http.client.RxHttpClient;
import io.micronaut.http.client.annotation.Client;
import io.micronaut.http.client.exceptions.HttpClientResponseException;
import io.micronaut.test.annotation.MicronautTest;
import io.micronaut.test.annotation.MockBean;
import org.apache.commons.lang3.RandomStringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Integration tests for the {@link AccountResource} REST controller.
 */
@MicronautTest(application = VcartApp.class, transactional = false)
@Property(name = "micronaut.security.enabled", value = "false")
public class AccountResourceIT {

    @Inject UserRepository userRepository;
    @Inject AuthorityRepository authorityRepository;
    @Inject UserService userService;
    @Inject MailService mailService;
    @Inject @Client("/") RxHttpClient client;

    @MockBean(UserService.class)
    UserService userService() {
        return mock(UserService.class);
    }

    @MockBean(MailService.class)
    MailService mailService() {
        return mock(MailService.class);
    }

    @BeforeEach
    public void setup() {
        doNothing().when(mailService).sendActivationEmail(any());
    }

    @Test
    public void testGetExistingAccount()  {
        Set<Authority> authorities = new HashSet<>();
        Authority authority = new Authority();
        authority.setName(AuthoritiesConstants.ADMIN);
        authorities.add(authority);

        User user = new User();
        user.setLogin("test");
        user.setFirstName("john");
        user.setLastName("doe");
        user.setEmail("john.doe@jhipster.com");
        user.setImageUrl("http://placehold.it/50x50");
        user.setLangKey("en");
        user.setAuthorities(authorities);
        when(userService.getUserWithAuthorities()).thenReturn(Optional.of(user));

        UserDTO userDTO = client.retrieve(HttpRequest.GET("/api/account"), UserDTO.class).blockingFirst();

        assertThat(userDTO.getLogin()).isEqualTo("test");
        assertThat(userDTO.getFirstName()).isEqualTo("john");
        assertThat(userDTO.getLastName()).isEqualTo("doe");
        assertThat(userDTO.getEmail()).isEqualTo("john.doe@jhipster.com");
        assertThat(userDTO.getImageUrl()).isEqualTo("http://placehold.it/50x50");
        assertThat(userDTO.getAuthorities().toString()).isEqualTo("[ROLE_ADMIN]");
    }


    @Test
    public void testGetUnknownAccount()  {
        when(userService.getUserWithAuthorities()).thenReturn(Optional.empty());

        HttpResponse<String> response = client.exchange(HttpRequest.GET("/api/account"), String.class).
            onErrorReturn(t -> (HttpResponse<String>) ((HttpClientResponseException) t).getResponse()).blockingFirst();

        assertThat(response.status().getCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.getCode());
    }

}
