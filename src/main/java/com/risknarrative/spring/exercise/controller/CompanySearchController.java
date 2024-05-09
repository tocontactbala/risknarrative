package com.risknarrative.spring.exercise.controller;

import com.risknarrative.spring.exercise.model.request.TruProxyApiRequest;
import com.risknarrative.spring.exercise.model.response.CompanySearchResponse;
import com.risknarrative.spring.exercise.service.TruProxyApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CompanySearchController {

    private TruProxyApiService truProxyApiClient;

    @Autowired
    public void CompanySearchController(TruProxyApiService truProxyApiClient) {
        this.truProxyApiClient = truProxyApiClient;
    }

    public CompanySearchController(TruProxyApiService truProxyApiClient) {
        this.truProxyApiClient = truProxyApiClient;
    }

    @PostMapping("/searchCompany")
    public CompanySearchResponse searchCompany(
            @RequestHeader(value = "x-api-key") String apiKey,
            @RequestBody TruProxyApiRequest request) {
        return truProxyApiClient.searchCompany(request.getCompanyName(), request.getCompanyNumber(), request.isActiveOnly());
    }
}

