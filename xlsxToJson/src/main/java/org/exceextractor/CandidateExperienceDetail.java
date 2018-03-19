package org.exceextractor;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateExperienceDetail {
    private CandidateEmployerDetail company;

    public CandidateEmployerDetail getCompany() {
        return company;
    }

    public void setCompany(CandidateEmployerDetail company) {
        this.company = company;
    }

    @Override
    public String toString() {
        return String.valueOf(company);
    }
}
