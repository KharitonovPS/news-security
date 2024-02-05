package org.kharitonov.person_security;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.kharitonov.person_security.models.ROLES;
import org.kharitonov.person_security.models.User;
import org.kharitonov.person_security.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

/**
 * @author Kharitonov Pavel on 05.02.2024.
 *
 * testing web lawyer
 */
@Tag("integration")
@Slf4j
@AutoConfigureMockMvc
@TestPropertySource(properties = {"spring.flyway.enabled=false"})
public class SecurityApplicationTests extends AbstractIntegrationServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository repository;

    private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private static final User USER = new User(1, "user",
            ENCODER.encode("user"), ROLES.ROLE_USER);
    private static final User ADMIN = new User(2, "admin",
            ENCODER.encode("admin"), ROLES.ROLE_ADMIN);


    @BeforeEach
    void setUp() {
        repository.deleteAll();
        log.info("preloading + " + repository.save(USER));
        log.info("preloading + " + repository.save(ADMIN));
    }

    @AfterAll
    static void afterAll() {
        POSTGRES.stop();
    }

    @Nested
    @Tag("/greetings endpoint")
    class ControllerTest {

        @Test
        @DisplayName("successful authentication")
        void shouldGreetAuthorizedUser() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/greetings")
                            .with(httpBasic("admin", "admin")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content()
                            .json("{\"greeting\":\"Hello, admin!\"}"));
        }

        @Test()
        @DisplayName("Unauthorized exception")
        void shouldTakeExceptionAuthorizedUser() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/greetings")
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test()
        @DisplayName("wrong password 401")
        void shouldTakeExceptionNotValidPassword() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/greetings")
                            .with(httpBasic("admin", "dummy")))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }
    }

    @Nested
    @Tag("admin")
    class AdminAuthoritiesTests {

        @Test
        @DisplayName("successful if admin")
        void shouldShowMessageIfPrincipalIsAdmin() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin")
                            .with(httpBasic("admin", "admin")))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(MockMvcResultMatchers.content()
                            .json("{\"greeting\":\"Hello, from admin!\"}"));
        }

        @Test
        @DisplayName("drop if user or guest")
        void shouldReturn403IfPrincipalIsNotAdmin() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/admin")
                            .with(httpBasic("user", "user")))
                    .andExpect(MockMvcResultMatchers.status().is(403));
        }

        @Test
        @DisplayName("make admin whit authorities")
        void shouldChangeRoleOfUserToAdminIfPrincipalRoleIsAdmin() throws Exception {
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/make-admin/user")
                            .with(httpBasic("admin", "admin")))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            User user = repository.findByUsername("user").orElseThrow();
            assertThat(user.getRole()).isEqualTo(ROLES.ROLE_ADMIN);
        }

        @Test
        @DisplayName("make admin without authorities")
        void shouldNotChangeRoleOfUserToAdminIfPrincipalRoleIsAdmin() throws Exception {
            repository.save(new User(3, "dummy", "dummy", ROLES.ROLE_GUEST));
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/make-admin/user")
                            .with(httpBasic("user", "user")))
                    .andExpect(MockMvcResultMatchers.status().is(403));

            User user = repository.findByUsername("dummy").orElseThrow();
            assertThat(user.getRole()).isEqualTo(ROLES.ROLE_GUEST);
        }
    }

    @Test
    @DisplayName("add user from controller")
    void shouldAddUserWithUniqueUsername() throws Exception {
        String requestBody = "{ \"username\": \"dummy\", \"password\": \"dummy\", \"role\": \"any\" }";

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertAll(
                () -> assertThat(repository.findAll().size()).isEqualTo(3),
                () -> assertThat(repository.findAll().stream()
                        .anyMatch(user -> user.getUsername().equals("dummy"))).isTrue()
        );
    }

    @Nested
    @Tag("validation")
    class ValidationTests {

        @Test
        @DisplayName("not unique username")
        void throwsExceptionIfUsernameAlreadyTaken() throws Exception {
            String requestBody = "{ \"username\": \"admin\", \"password\": \"any\", \"role\": \"any\" }";
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
            var result = resultActions.andDo(print()).andReturn();
            var response = result.getResponse();
            assertAll(
                    () -> assertThat(response.getContentAsString().contains("Username is already taken or empty")).isTrue(),
                    () -> assertThat(repository.findAll().size()).isEqualTo(2)
            );
        }

        @Test
        @DisplayName("empty username")
        void throwsExceptionIfUsernameIsEmpty() throws Exception {
            String requestBody = "{ \"username\": \"\", \"password\": \"any\", \"role\": \"any\" }";
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
            var result = resultActions.andDo(print()).andReturn();
            var response = result.getResponse();
            assertAll(
                    () -> assertThat(response.getContentAsString().contains("Username is already taken or empty")).isTrue(),
                    () -> assertThat(repository.findAll().size()).isEqualTo(2)
            );
        }

        @Test
        @DisplayName("null username")
        void throwsExceptionIfUsernameIsNull() throws Exception {

            String requestBody = "{ \"username\": null, \"password\": \"any\", \"role\": \"any\" }";

            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
            var result = resultActions.andDo(print()).andReturn();
            var response = result.getResponse();
            assertAll(
                    () -> assertThat(response.getContentAsString().contains("Username is already taken or empty")).isTrue(),
                    () -> assertThat(repository.findAll().size()).isEqualTo(2)
            );

        }

        @Test
        @DisplayName("numeric username")
        void throwsExceptionIfUsernameIsNumber() throws Exception {
            String requestBody = "{ \"username\": 123, \"password\": \"any\", \"role\": \"any\" }";
            ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().is4xxClientError());
            var result = resultActions.andDo(print()).andReturn();
            var response = result.getResponse();
            assertAll(
                    () -> assertThat(response.getContentAsString().contains("Username is already taken or empty")).isTrue(),
                    () -> assertThat(repository.findAll().size()).isEqualTo(2)
            );
        }
        @Test
        @Tag("encryption")
        @DisplayName("Test password encryption algorithm configuration")
        void testPasswordEncryption() throws Exception {
            String requestBody = "{ \"username\": \"testUser\", \"password\": \"testPassword\", \"role\": \"any\" }";
            mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/registration")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(requestBody))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            User savedUser = repository.findByUsername("testUser").orElse(null);
            assertAll(
                    () -> assertThat(savedUser).isNotNull(),
                    () -> assertThat(savedUser.getPassword()).isNotEqualTo("testPassword")
            );
        }

    }
}
