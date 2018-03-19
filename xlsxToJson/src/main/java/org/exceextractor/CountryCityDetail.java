package org.exceextractor;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryCityDetail {
    private String city;
    private Long id;
    private CountryDetail country;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public CountryDetail getCountry() {
        return country;
    }

    public void setCountry(CountryDetail country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return
                "city='" + city + '\'' +
                ", id=" + id +
                ", country=" + country +
                '}';
    }
}
