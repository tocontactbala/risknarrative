package com.risknarrative.spring.exercise.controller.service;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.risknarrative.spring.exercise.model.request.TruProxyApiRequest;
import com.risknarrative.spring.exercise.model.response.CompanySearchResponse;
import com.risknarrative.spring.exercise.service.TruProxyApiService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TruProxyApiClientTest {

    private static WireMockServer wireMockServer;

    private TruProxyApiService truProxyApiService;

    @BeforeAll
    public static void setUp() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void searchCompanyTest() throws Exception {
        // Arrange
        wireMockServer.stubFor(post(urlEqualTo("/search"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody("{\"total_results\":1,\"items\":[{\"company_number\":\"06500244\",\"company_type\":\"ltd\",\"title\":\"BBC LIMITED\",\"company_status\":\"active\",\"date_of_creation\":\"2008-02-11\",\"address\":{\"locality\":\"Retford\",\"postal_code\":\"DN22 0AD\",\"premises\":\"Boswell Cottage Main Street\",\"address_line_1\":\"North Leverton\",\"country\":\"England\"},\"officers\":[{\"name\":\"BOXALL, Sarah Victoria\",\"officer_role\":\"secretary\",\"appointed_on\":\"2008-02-11\",\"address\":{\"premises\":\"5\",\"locality\":\"London\",\"address_line_1\":\"Cranford Close\",\"country\":\"England\",\"postal_code\":\"SW20 0DP\"}}]}]")));

        TruProxyApiRequest request = new TruProxyApiRequest();
        request.setCompanyName("BBC LIMITED");
        request.setCompanyNumber("06500244");
        request.setActiveOnly(true);

        RestTemplate restTemplate = new RestTemplateBuilder().build();
        truProxyApiService = new TruProxyApiService(restTemplate);
        truProxyApiService.setBaseUrl("http://localhost:8089");
        truProxyApiService.setApiKey("PwewCEztSW7XlaAKqkg4IaOsPelGynw6SN9WsbNf");

        CompanySearchResponse response = truProxyApiService.searchCompany("BBC LIMITED", "06500244", true);

        assertEquals(1, response.getResults());
        assertEquals("06500244", response.getItems().get(0).getCompanyNumber());
        assertEquals("BBC LIMITED", response.getItems().get(0).getTitle());
        assertEquals("active", response.getItems().get(0).getCompanyStatus());
        assertEquals("BOXALL, Sarah Victoria", response.getItems().get(0).getOfficers().get(0).getName());
        assertEquals("secretary", response.getItems().get(0).getOfficers().get(0).getOfficer_role());
    }
}
