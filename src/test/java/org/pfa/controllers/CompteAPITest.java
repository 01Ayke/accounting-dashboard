package org.pfa.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import org.junit.jupiter.api.*;
import org.pfa.models.dto.FileAPIResponse;
import org.pfa.routes.CompteAuxiliaireRoutes;
import org.pfa.routes.FileRoutes;
import org.pfa.routes.UserRoutes;
import org.pfa.services.CompteService;
import org.pfa.services.FileService;
import org.pfa.services.SessionManager;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CompteAPITest {
    private Javalin app;
    private HttpClient client;
    private ObjectMapper objectMapper = new ObjectMapper();
    private String baseUrl;
    private FileService fileService = new FileService();
    private CompteService compteService = new CompteService();

    private String token;
    private Integer idDossierCree;

    private String fileJson = "{\n" +
            "    \"telephone\": \"0707070707\",\n" +
            "    \"mail\": \"mail\",\n" +
            "    \"denominationSociale\": \"Test Enterprise4\",\n" +
            "    \"formeJuridique\" : {\n" +
            "        \"idFormeJuridique\" : 1\n" +
            "    },\n" +
            "    \"regimeFiscal\" : {\n" +
            "        \"idRegimeFiscal\" : 1\n" +
            "    },\n" +
            "    \"regimeTVA\" : {\n" +
            "        \"idRegimeTVA\" : 1   },\n" +
            "    \"adresse\": {\n" +
            "        \"pays\": \"Testistan\",\n" +
            "        \"ville\": \"TestVille\",\n" +
            "        \"rue\": \"Test\",\n" +
            "        \"numero\" : \"2\",\n" +
            "        \"complement\": \"complement\",\n" +
            "        \"codePostal\": \"33000\"\n" +
            "    }\n" +
            "}";

    @BeforeAll
    void setUp() throws IOException, InterruptedException {
        app = Javalin.create().start(0); // port 0 => random port
        SessionManager sessionManager = new SessionManager();
        FileRoutes.register(app, sessionManager);
        UserRoutes.register(app, sessionManager);
        CompteAuxiliaireRoutes.register(app, sessionManager);
        baseUrl = "http://localhost:" + app.port();
        client = HttpClient.newHttpClient();

        // login
        String body = "mail=admin@admin.fr&password=admin";

        HttpRequest loginRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/login"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        token = (String) objectMapper
                .readValue(client.send(loginRequest, HttpResponse.BodyHandlers.ofString()).body(), Map.class).get("sID");
    }

    @BeforeEach
    void insert() throws IOException, InterruptedException {
        // file insertion
        HttpRequest fileRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/file"))
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .POST(HttpRequest.BodyPublishers.ofString(fileJson))
                .build();

        HttpResponse<String> response = client.send(fileRequest, HttpResponse.BodyHandlers.ofString());
        idDossierCree = objectMapper.readValue(response.body().toString(), FileAPIResponse.class).getMessage().get("idDossier");
        System.out.println(idDossierCree);
    }

    @AfterEach
    void cleanUp() {
        fileService.deleteByName("Test Enterprise4");
    }

    @AfterAll
    void tearDown() {
        app.stop();
    }

    @Test
    @DisplayName("Success on compte insertion")
    void testCompteInsertion() throws IOException, InterruptedException {
        assertTrue(true);
        Integer idCompte = compteService.findByFileAndNumero(fileService.getOne(idDossierCree), "411").getIdCompte();
        String compteJson = "{\n" +
                "    \"idTypeCompteAuxiliaire\": 1,\n" +
                "    \"idCompteRattachement\":" + idCompte + ",\n" +
                "    \"numero\": \"41102001\",\n" +
                "    \"libelle\": \"libella\"\n" +
                "}";
        HttpRequest compteRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/compte"))
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .POST(HttpRequest.BodyPublishers.ofString(compteJson))
                .build();

        HttpResponse<String> response = client.send(compteRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());
    }
}
