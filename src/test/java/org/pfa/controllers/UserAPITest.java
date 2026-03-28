package org.pfa.controllers;

import io.javalin.Javalin;
import org.junit.jupiter.api.*;
import org.pfa.routes.UserRoutes;
import org.pfa.services.SessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.pfa.repositories.UserRepository;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserAPITest {
    private Javalin app;
    private HttpClient client;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrl;
    private final Set<String> createdEmails = new HashSet<>();
    private final UserRepository userRepository = new UserRepository();

    @BeforeAll
    void setUp() {
        app = Javalin.create().start(0); // port 0 => random port
        SessionManager sessionManager = new SessionManager();
        UserRoutes.register(app, sessionManager);
        baseUrl = "http://localhost:" + app.port();
        client = HttpClient.newHttpClient();
    }

    @AfterAll
    void tearDown() {
        app.stop();
        // nettoyage des users crées
        for (String email : createdEmails) {
            try {
                userRepository.deleteByMail(email);
                System.out.println("\u001B[34m[CLEANUP] Deleted test user: " + email + "\u001B[0m");
            } catch (Exception e) {
                System.err.println("Failed to cleanup user: " + email + " - " + e.getMessage());
            }
        }
    }

    @Test
    @DisplayName("Login Success with Admin Credentials")
    void testLoginSuccess() throws IOException, InterruptedException {
        String body = "mail=admin@admin.fr&password=admin";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
        Map<String, Object> json = objectMapper.readValue(response.body(), Map.class);
        assertEquals("success", json.get("status"));
        assertNotNull(json.get("sID"));
    }

    @Test
    @DisplayName("Login Failure with Invalid Credentials")
    void testLoginFailure() throws IOException, InterruptedException {
        String body = "mail=wrong@admin.fr&password=wrong";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(401, response.statusCode());
    }

    @Test
    @DisplayName("Logout Success")
    void testLogoutSuccess() throws IOException, InterruptedException {
        // login
        String loginBody = "mail=admin@admin.fr&password=admin";
        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                .build();
        HttpResponse<String> loginResponse = client.send(loginRequest, HttpResponse.BodyHandlers.ofString());
        String token = (String) objectMapper.readValue(loginResponse.body(), Map.class).get("sID");

        // logout
        HttpRequest logoutRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/logout"))
                .header("Authorization", token)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        HttpResponse<String> logoutResponse = client.send(logoutRequest, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, logoutResponse.statusCode());
    }

    @Test
    @DisplayName("Register New User")
    void testRegisterSuccess() throws IOException, InterruptedException {
        String loginBody = "mail=admin@admin.fr&password=admin";
        HttpRequest loginReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(loginBody))
                .build();
        String token = (String) objectMapper
                .readValue(client.send(loginReq, HttpResponse.BodyHandlers.ofString()).body(), Map.class).get("sID");

        String email = "user" + System.currentTimeMillis() + "@pfa.fr";
        createdEmails.add(email);
        String registerBody = "mail=" + email + "&password=password&nom=Test&prenom=User&code=C"+System.currentTimeMillis()+"&roles=EXPERT";
                

        HttpRequest regReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/register"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", token)
                .POST(HttpRequest.BodyPublishers.ofString(registerBody))
                .build();

        HttpResponse<String> response = client.send(regReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    @DisplayName("Functional Flow: Admin registers user, User fails to register another, Logout")
    void testFunctionalRegistrationFlow() throws IOException, InterruptedException {
        // login en tant qu'admin
        String adminLoginBody = "mail=admin@admin.fr&password=admin";
        HttpRequest adminLoginReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(adminLoginBody))
                .build();
        HttpResponse<String> adminLoginRes = client.send(adminLoginReq, HttpResponse.BodyHandlers.ofString());
        String adminToken = (String) objectMapper.readValue(adminLoginRes.body(), Map.class).get("sID");

        // admin cree user B
        String userBMail = "userB" + System.currentTimeMillis() + "@test.fr";
        createdEmails.add(userBMail);
        String userBPass = "passwordB";
        String registerBody = "mail=" + userBMail + "&password=" + userBPass + "&nom=User&prenom=B&code=UB"
                + System.currentTimeMillis();
        HttpRequest registerReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/register"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", adminToken)
                .POST(HttpRequest.BodyPublishers.ofString(registerBody))
                .build();
        HttpResponse<String> registerRes = client.send(registerReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, registerRes.statusCode(), "Admin should be able to register User B");

        // login en tant qu'user B
        String userBLoginBody = "mail=" + userBMail + "&password=" + userBPass;
        HttpRequest userBLoginReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(userBLoginBody))
                .build();
        HttpResponse<String> userBLoginRes = client.send(userBLoginReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, userBLoginRes.statusCode(), "User B should be able to login");
        String userBToken = (String) objectMapper.readValue(userBLoginRes.body(), Map.class).get("sID");

        // user B essaie de creer user C
        String userCMail = "userC" + System.currentTimeMillis() + "@test.fr";
        String registerCBody = "mail=" + userCMail + "&password=passC&nom=User&prenom=C&code=UC"
                + System.currentTimeMillis();
        HttpRequest registerCReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/register"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .header("Authorization", userBToken)
                .POST(HttpRequest.BodyPublishers.ofString(registerCBody))
                .build();
        HttpResponse<String> registerCRes = client.send(registerCReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(403, registerCRes.statusCode(), "Non-admin User B should NOT be able to register User C");

        // logout en tant qu'user B
        HttpRequest logoutReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/logout"))
                .header("Authorization", userBToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> logoutRes = client.send(logoutReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, logoutRes.statusCode(), "User B should be able to logout");

        // logout en tant qu'admin
        HttpRequest adminLogoutReq = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/logout"))
                .header("Authorization", adminToken)
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> adminLogoutRes = client.send(adminLogoutReq, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, adminLogoutRes.statusCode(), "Admin should be able to logout");
    }

    @Test
    @DisplayName("Register Without Login (401)")
    void testRegisterUnauthorized() throws IOException, InterruptedException {
        String body = "mail=toto@pfa.fr&password=toto&nom=testUSER&prenom=testUSER&code=testUSER";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/register"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(401, response.statusCode());
    }
}
