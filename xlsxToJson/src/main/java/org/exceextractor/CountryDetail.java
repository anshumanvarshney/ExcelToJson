package org.exceextractor;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CountryDetail {
	private String country;

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

    @Override
    public String toString() {
        return "org.exceextractor.CountryDetail{" +
                "country='" + country + '\'' +
                '}';
    }
}
