package com.risknarrative.spring.exercise.controller.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.risknarrative.spring.exercise.model.request.TruProxyApiRequest;
import com.risknarrative.spring.exercise.model.response.Address;
import com.risknarrative.spring.exercise.model.response.Company;
import com.risknarrative.spring.exercise.model.response.CompanySearchResponse;
import com.risknarrative.spring.exercise.model.response.Officer;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class CompanySearchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private int port;

    private static WireMockServer wireMockServer;

    @BeforeAll
    public static void beforeAll() {
        wireMockServer = new WireMockServer(8089);
        wireMockServer.start();
        WireMock.configureFor("localhost", 8089);
    }

    @AfterAll
    public static void afterAll() {
        wireMockServer.stop();
    }

    @Test
    public void searchCompanyIntegrationTest() throws Exception {
        // Arrange
        TruProxyApiRequest request = new TruProxyApiRequest();
        request.setCompanyName("BBC LIMITED");
        request.setCompanyNumber("06500244");
        request.setActiveOnly(true);

        CompanySearchResponse mockResponse = new CompanySearchResponse();
        // Set up mock response
        Company company = new Company();
        company.setCompanyNumber("06500244");
        company.setCompanyType("ltd");
        company.setTitle("BBC LIMITED");
        company.setCompanyStatus("active");
        company.setDateOfCreation("2008-02-11");

        Address address = new Address();
        address.setLocality("Retford");
        address.setPostal_code("DN22 0AD");
        address.setPremises("Boswell Cottage Main Street");
        address.setAddress_line_1("North Leverton");
        address.setCountry("England");
        company.setAddress(address);

        Officer officer = new Officer();
        officer.setName("BOXALL, Sarah Victoria");
        officer.setOfficer_role("secretary");
        officer.setAppointed_on("2008-02-11");

        Address officerAddress = new Address();
        officerAddress.setPremises("5");
        officerAddress.setLocality("London");
        officerAddress.setAddress_line_1("Cranford Close");
        officerAddress.setCountry("England");
        officerAddress.setPostal_code("SW20 0DP");
        officer.setAddress(officerAddress);

        company.setOfficers(List.of(officer));

        mockResponse.setResults(1);
        mockResponse.setItems(List.of(company));

        // Stubbing the WireMock server
        wireMockServer.stubFor(post(urlEqualTo("/search"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .withBody(objectMapper.writeValueAsString(mockResponse))));

        // Act and Assert
        mockMvc.perform(MockMvcRequestBuilders.post("/searchCompany")
                        .header("x-api-key", "your-api-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.jsonPath("$.total_results").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].company_number").value("06500244"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].title").value("BBC LIMITED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].company_status").value("active"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].officers[0].name").value("BOXALL, Sarah Victoria"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.items[0].officers[0].officer_role").value("secretary"));
    }
}