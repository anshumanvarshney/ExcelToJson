package org.exceextractor;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SetPojo {

    private String name;
    private String email;

    @JsonProperty("experience_months")
    private Integer workExpMonths;
    private String linkedInUrl;
    private CountryCityDetail countryCityDetail;

    private List<CandidateSkillDetail> skills;

    private List<CandidateExperienceDetail> experience;
    private List<CandidateEducationDetail> education;
    private InitialInformation application;

    public Integer getWorkExpMonths() {
        return workExpMonths;
    }

    public void setWorkExpMonths(Integer workExpMonths) {
        this.workExpMonths = workExpMonths;
    }

    public InitialInformation getApplication() {
        return application;
    }

    public void setApplication(InitialInformation application) {
        this.application = application;
    }

    public CountryCityDetail getCountryCityDetail() {
        return countryCityDetail;
    }

    public void setCountryCityDetail(CountryCityDetail countryCityDetail) {
        this.countryCityDetail = countryCityDetail;
    }

    public List<CandidateSkillDetail> getSkills() {
        return skills;
    }

    public void setSkills(List<CandidateSkillDetail> skills) {
        this.skills = skills;
    }

    public List<CandidateExperienceDetail> getExperience() {
        return experience;
    }

    public void setExperience(List<CandidateExperienceDetail> experience) {
        this.experience = experience;
    }

    public List<CandidateEducationDetail> getEducation() {
        return education;
    }

    public void setEducation(List<CandidateEducationDetail> education) {
        this.education = education;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getLinkedInUrl() {
        return linkedInUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setLinkedInUrl(String linkedInUrl) {
        this.linkedInUrl = linkedInUrl;
    }

    @Override
    public String toString() {
        return "org.exceextractor.SetPojo{" +
                "skills=" + skills +
                ", experience=" + experience +
                '}';
    }


}

