package com.risknarrative.spring.exercise.service;

import com.risknarrative.spring.exercise.model.request.TruProxyApiRequest;
import com.risknarrative.spring.exercise.model.response.CompanySearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TruProxyApiService {

    @Value("${truProxyApi.baseUrl}")
    private String baseUrl;

    @Value("${truProxyApi.apiKey}")
    private String apiKey;

    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    private final RestTemplate restTemplate;

    public TruProxyApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public CompanySearchResponse searchCompany(String companyName, String companyNumber, boolean activeOnly) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", apiKey);

        TruProxyApiRequest request = new TruProxyApiRequest();
        request.setCompanyName(companyName);
        request.setCompanyNumber(companyNumber);
        request.setActiveOnly(activeOnly);

        HttpEntity<TruProxyApiRequest> entity = new HttpEntity<>(request, headers);

        ResponseEntity<CompanySearchResponse> response = restTemplate.postForEntity(baseUrl + "/search", entity, CompanySearchResponse.class);
        return response.getBody();
    }
}

