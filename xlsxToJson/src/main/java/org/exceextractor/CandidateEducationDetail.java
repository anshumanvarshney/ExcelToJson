package org.exceextractor;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.sql.Timestamp;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class CandidateEducationDetail {
    private String education;
    private String degree;
    private CandidateSchoolDetail school;
    private Timestamp startDate;
    private Timestamp endDate;

    public String getDegree() {
        return degree;
    }

    public void setDegree(String degree) {
        this.degree = degree;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public CandidateSchoolDetail getSchool() {
        return school;
    }

    public void setSchool(CandidateSchoolDetail school) {
        this.school = school;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return
                "education='" + education + '\'' +
                        ", school=" + school +
                        ", startDate=" + startDate +
                        ", endDate=" + endDate;
    }
}
