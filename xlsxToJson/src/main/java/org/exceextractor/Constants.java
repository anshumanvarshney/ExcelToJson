package org.exceextractor;

public class Constants {
    private Constants(){}

    public static final String CURL_PART = "curl -X POST \\\n" +
            "  http://172.21.0.7:8120/v3/customer/2392/candidate \\\n" +
            "  -H 'cache-control: no-cache' \\\n" +
            "  -H 'content-type: application/json' \\\n" +
            "  -H 'customerid: 2392' \\\n" +
            "  -H 'postman-token: e49cb0f7-255f-015b-a92c-e9ec9f6d8830' \\\n" +
            "  -H 'userid: 4378' \\\n" +
            "  -d '";
    public static final String FINAL_PART = "'";
    public static final String EXTENSION = ".sh";
    public static final String SEMICOLON = ";";
    public static final String EMPTYSPACE = "";
    public static final String REGEX_GENERAL = "[^A-Za-z ]+";
    public static final String REGEX_NAME = "[^A-Za-z .]+";
    public static final String REGEX_PATTERN = "\\( ?\\d{4}([^()])*\\)";
    public static final String NAME = "Name";
    public static final String EMAIL = "E-mail Address";
    public static final String LINKEDIN = "Scan URL";
    public static final String EXPEREINCE = "Scan Expereince";
    public static final String COMPANY = "Scan Company";
    public static final String LOCATION = "Scan Location";
    public static final String ENDORSEMENT = "Scan Endorsements";
    public static final String EDUCATION = "Scan Qualification";
    public static final String DEFAULT_EDUCATION = "ATS_GRADUATION";
    public static final String DEFAULT_APPLICATION = "2018-02-28T06:19:42.310Z";
    public static final String DEFAULT_CODE = "NONE";
    public static final String DEFAULT_NAME = "TH_ATS_NONE";
    public static final String COMMA = ",";
    public static final String YEAR_PATTERN = "yyyy";
    public static final String EMAIL_REGEX = "(\\b([a-zA-Z0-9\\.\\+-_]+@\\S+[.A-Za-z])\\b)";
    public static final String CITY_EXTRA_GARBAGE = "Area";
    public static final String DASH_PATTERN = "-";
    public static final String ERROR = "file not found";
    public static final String URL_DATA  = "/Users/anshumanvarshney/Desktop/Errors/-.sql";
}