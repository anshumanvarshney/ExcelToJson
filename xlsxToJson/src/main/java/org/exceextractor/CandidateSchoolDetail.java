package org.exceextractor;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateSchoolDetail {
    private String school;
    private CountryCityDetail countryCity;

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public CountryCityDetail getCountryCity() {
        return countryCity;
    }

    public void setCountryCity(CountryCityDetail countryCity) {
        this.countryCity = countryCity;
    }

    @Override
    public String toString() {
        return
                "school='" + school + '\'' +
                        ", countryCity=" + countryCity;
    }
}
