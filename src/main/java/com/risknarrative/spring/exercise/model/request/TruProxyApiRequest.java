package com.risknarrative.spring.exercise.model.request;

public class TruProxyApiRequest {
    private String companyName;
    private String companyNumber;
    private boolean activeOnly;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyNumber() {
        return companyNumber;
    }

    public void setCompanyNumber(String companyNumber) {
        this.companyNumber = companyNumber;
    }

    public boolean isActiveOnly() {
        return activeOnly;
    }

    public void setActiveOnly(boolean activeOnly) {
        this.activeOnly = activeOnly;
    }
}
