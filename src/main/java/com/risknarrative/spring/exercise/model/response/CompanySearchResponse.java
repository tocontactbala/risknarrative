package com.risknarrative.spring.exercise.model.response;

import java.util.List;

public class CompanySearchResponse {
    private int results;
    private List<Company> items;

    public int getResults() {
        return results;
    }

    public void setResults(int results) {
        this.results = results;
    }

    public List<Company> getItems() {
        return items;
    }

    public void setItems(List<Company> items) {
        this.items = items;
    }
}







