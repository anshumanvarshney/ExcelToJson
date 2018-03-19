package org.exceextractor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Anshuman Varshney on 1/3/18.
 */
public class ReadFromExcel {

    @Value("${json.path}")
    private String jsonPath;

    @Value("${curl.path}")
    private String curlPath;

    @Value("${excel.path}")
    private String excelPath;


//    static Logger log = Logger.getLogger(ReadFromExcel.class.getName());

    /**
     * writing json files from content
     *
     * @param listOfDataFromReport
     * @throws IOException
     */
    public void writeJsonUsingObjectMapper(List<SetPojo> listOfDataFromReport) throws IOException {
        for (int index = 0; index < 26836; index++) {
           // log.info("writing " + index);
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(
                    new FileOutputStream(jsonPath + index), listOfDataFromReport.get(index));

        }
    }

    /**
     * cleaning the experience
     *
     * @param stringCellValue
     * @return
     */
    private static int cleanExperience(String stringCellValue) {
        String[] experience = stringCellValue.split(Constants.SEMICOLON);
        String expStr = experience[0];
        if (!expStr.equals(Constants.EMPTYSPACE)) {
            int exp = 0;
            try {
                exp = Integer.parseInt(expStr);
            } catch (Exception e) {
                exp = Double.valueOf(expStr).intValue();
            }
            return exp;
        } else return 0;
    }

    /**
     * reading Application prefix
     *
     * @param setObject
     */
    private void readApplication(SetPojo setObject) {
        InitialInformation initialInformation = new InitialInformation();
        initialInformation.setDateOfApplication(Constants.DEFAULT_APPLICATION);
        initialInformation.setCode(Constants.DEFAULT_CODE);
        initialInformation.setName(Constants.DEFAULT_NAME);
        setObject.setApplication(initialInformation);
    }

    /**
     * get the cityCountry Id
     *
     * @return
     */
    private static Map<String, Long> getCityCountryId() {
        String filePath = Constants.URL_DATA;
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
           // log.error("Reading Failed");
        }

        String[] listOfString = new String[0];
        if (null != content)
            listOfString = content.split("\n");
        Map<String, Long> mapOfCountryCity = new HashMap<>();
        for (String string : listOfString) {
            mapOfCountryCity.put(string.split("\\|")[5].trim(), Long.valueOf(string.split("\\|")[1].trim()));
        }
        return mapOfCountryCity;
    }

    /**
     * Reading Names
     *
     * @param dataRow
     * @param map
     * @param setObject
     */
    public void readName(XSSFRow dataRow, Map<String, Integer> map, SetPojo setObject) {
        int indexName = map.get(Constants.NAME);

        Optional.ofNullable(dataRow)
                .map(s -> s.getCell(indexName))
                .map(XSSFCell::getStringCellValue)
                .ifPresent(s -> setObject.setName(s.replaceAll(Constants.REGEX_NAME, Constants.EMPTYSPACE)));
    }

    /**
     * Reading Email
     *
     * @param dataRow
     * @param map
     * @param setObject
     */
    public void readEmail(XSSFRow dataRow, Map<String, Integer> map, SetPojo setObject) {
        int indexEmail = map.get(Constants.EMAIL);
        XSSFCell cellEmail;
        if (null != dataRow.getCell(indexEmail)) {
            cellEmail = dataRow.getCell(indexEmail);
            Pattern pattern = Pattern.compile(Constants.EMAIL_REGEX);
            Matcher matcher = pattern.matcher(cellEmail.getStringCellValue());
            if (matcher.find()) {
                setObject.setEmail(cellEmail.getStringCellValue());
            }
        }
    }

    /**
     * Reading LinkedInURL
     *
     * @param dataRow
     * @param map
     * @param setObject
     */
    public void readLinkedIn(XSSFRow dataRow, Map<String, Integer> map, SetPojo setObject) {
        int indexLinkedIn = map.get(Constants.LINKEDIN);
        XSSFCell cellLinkedIn;

        if (null != dataRow.getCell(indexLinkedIn)) {
            cellLinkedIn = dataRow.getCell(indexLinkedIn);
            setObject.setLinkedInUrl(cellLinkedIn.getStringCellValue());
        }
    }

    /**
     * Reading Number of years of Experience
     *
     * @param dataRow
     * @param map
     * @param setObject
     */
    public void readExperience(XSSFRow dataRow, Map<String, Integer> map, SetPojo setObject) {
        int indexExperience = map.get(Constants.EXPEREINCE);
        XSSFCell cellExperience;

        if (null != dataRow.getCell(indexExperience)) {
            cellExperience = dataRow.getCell(indexExperience);
            String expValue = null;
            try {
                expValue = cellExperience.getStringCellValue();
            } catch (Exception e) {
                expValue = String.valueOf(cellExperience.getNumericCellValue());
            }
            int exp = cleanExperience(expValue);
            if (exp != 0) {
                setObject.setWorkExpMonths(exp * 12);
            }
        }
    }

    /**
     * Reading Company Names
     *
     * @param dataRow
     * @param map
     * @param setObject
     */
    public void readCompany(XSSFRow dataRow, Map<String, Integer> map, SetPojo setObject) {
        int indexCompany = map.get(Constants.COMPANY);

        Optional.ofNullable(dataRow)
                .map(s -> s.getCell(indexCompany))
                .map(this::getCandidateExperienceDetails)
                .ifPresent(setObject::setExperience);
    }

    private List<CandidateExperienceDetail> getCandidateExperienceDetails(XSSFCell s) {
        String companyName = s.getStringCellValue().replace(Constants.SEMICOLON, Constants.EMPTYSPACE).trim();
        List<CandidateExperienceDetail> listOfCompany = new ArrayList<>();
        CandidateExperienceDetail candidateExperienceDetail = new CandidateExperienceDetail();
        CandidateEmployerDetail candidateEmployerDetail = new CandidateEmployerDetail();
        candidateEmployerDetail.setCompany(companyName.replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
        candidateExperienceDetail.setCompany(candidateEmployerDetail);
        listOfCompany.add(candidateExperienceDetail);
        return listOfCompany;
    }

    /**
     * Reading Location i.e city and country
     *
     * @param dataRow
     * @param map
     * @param setObject
     * @param mapOfCountryCity
     */
    public void readLocation(XSSFRow dataRow, Map<String, Integer> map, SetPojo setObject,
                             Map<String, Long> mapOfCountryCity) {
        int indexLocation = map.get(Constants.LOCATION);
        XSSFCell cellLocation;
        if (null != dataRow.getCell(indexLocation)) {
            cellLocation = dataRow.getCell(indexLocation);
            String[] cityCountry = cellLocation.getStringCellValue().split(Constants.COMMA);
            CountryCityDetail countryCityDetail = new CountryCityDetail();
            CountryDetail countryDetail = new CountryDetail();
            if (cityCountry.length == 1) {
                whenOnlyCityPresent(countryDetail, cityCountry, countryCityDetail, setObject);
            } else if (cityCountry.length == 2) {
                whenCountryAndCityPresent(countryDetail, cityCountry, countryCityDetail, setObject, mapOfCountryCity);
            } else if (cityCountry.length == 3) {
                whenCountryAndCityAndStatePresent(countryDetail, cityCountry, countryCityDetail, setObject,
                        mapOfCountryCity);
            }
        }
    }

    public void whenOnlyCityPresent(CountryDetail countryDetail, String[] cityCountry,
                                    CountryCityDetail countryCityDetail, SetPojo setObject) {
        countryDetail.setCountry(cityCountry[0].trim().replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
        countryCityDetail.setCountry(countryDetail);
        setObject.setCountryCityDetail(countryCityDetail);
    }

    public void whenCountryAndCityPresent(CountryDetail countryDetail, String[] cityCountry,
                                          CountryCityDetail countryCityDetail, SetPojo setObject,
                                          Map<String, Long> mapOfCountryCity) {
        String city = cityCountry[0].trim().replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE);
        city = city.replaceAll(Constants.CITY_EXTRA_GARBAGE, Constants.EMPTYSPACE).trim();
        String country = cityCountry[1].trim().replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE);
        city = city.replace(Constants.CITY_EXTRA_GARBAGE, Constants.EMPTYSPACE);
        city = city.trim();
        String countryCity = country + Constants.DASH_PATTERN + city;
        countryCityDetail.setId(mapOfCountryCity.get(countryCity));
        countryCityDetail.setCity(city);
        countryDetail.setCountry(country);
        countryCityDetail.setCountry(countryDetail);
        setObject.setCountryCityDetail(countryCityDetail);
    }

    public void whenCountryAndCityAndStatePresent(CountryDetail countryDetail, String[] cityCountry,
                                                  CountryCityDetail countryCityDetail, SetPojo setObject,
                                                  Map<String, Long> mapOfCountryCity) {
        String city = cityCountry[0].trim().replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE);
        String country = cityCountry[2].trim().replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE);
        city = city.replace(Constants.CITY_EXTRA_GARBAGE, Constants.EMPTYSPACE);
        city = city.trim();
        String countryCity = country + Constants.DASH_PATTERN + city;
        countryCityDetail.setId(mapOfCountryCity.get(countryCity));
        countryCityDetail.setCity(city);
        countryDetail.setCountry(country);
        countryCityDetail.setCountry(countryDetail);
        setObject.setCountryCityDetail(countryCityDetail);
    }

    /**
     * Reading SkillSet
     *
     * @param dataRow
     * @param map
     * @param setObject
     */
    public void readSkills(XSSFRow dataRow, Map<String, Integer> map, SetPojo setObject) {
        int indexSkills = map.get(Constants.ENDORSEMENT);
        XSSFCell cellSkills;
        if (null != dataRow.getCell(indexSkills)) {
            cellSkills = dataRow.getCell(indexSkills);
            if (!cellSkills.toString().equals(Constants.EMPTYSPACE)) {

                String[] skillString = cellSkills.getStringCellValue().split(Constants.SEMICOLON);
                List<CandidateSkillDetail> skillList = new ArrayList<>();
                for (String skill : skillString) {
                    CandidateSkillDetail candidateSkillDetail = new CandidateSkillDetail();
                    candidateSkillDetail.setSkillName(skill.replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
                    skillList.add(candidateSkillDetail);
                }
                setObject.setSkills(skillList);
            }
        }
    }

    /**
     * Reading Education Details i.e degree, CollegeName, City, StartYear and EndYear
     *
     * @param dataRow
     * @param map
     * @param setObject
     * @throws ParseException
     */
    public void readEducation(XSSFRow dataRow, Map<String, Integer> map, SetPojo setObject) throws ParseException {
        int indexEducation = map.get(Constants.EDUCATION);
        XSSFCell cellEducation;


        if (null != dataRow.getCell(indexEducation)) {
            cellEducation = dataRow.getCell(indexEducation);
            if (null != cellEducation.getStringCellValue().trim()) {
                extractingEducationDetails(cellEducation, setObject);
            }
        }
    }

    private void extractingEducationDetails(XSSFCell cellEducation, SetPojo setObject) throws ParseException {
        String[] educationSetString = cellEducation.getStringCellValue().split(Constants.SEMICOLON);
        List<CandidateEducationDetail> listofGetEducation = new ArrayList<>();
        for (String education : educationSetString) {
            String[] educationString = education.split(Constants.COMMA);
            if (educationString.length == 1) {
                onlyCollegeNameIsGiven(educationString, listofGetEducation);
            } else if (educationString.length == 2) {
                onlyCollegeNameAndCityGiven(educationString, listofGetEducation);
            } else if (educationString.length == 3) {
                onlyCollegeNameAndCityAndDegreeGiven(educationString, listofGetEducation);
            }
        }
        setObject.setEducation(listofGetEducation);
    }

    private List<Integer> extractYearsUtil(String yearStrToEnd) {

        int startYear = 0;
        int endYear = 0;
        List<Integer> yearList = new ArrayList<>();

        String[] year = new String[10];
        if (yearStrToEnd.contains("-–")) {
            year = yearStrToEnd.split("-–");
        } else if (yearStrToEnd.contains("–")) {
            year = yearStrToEnd.split("–");
        } else if (yearStrToEnd.contains("-")) {
            year = yearStrToEnd.split("-");
        } else if (yearStrToEnd.length() == 8) {
            year[0] = yearStrToEnd.substring(0, 4);
            year[1] = yearStrToEnd.substring(4, yearStrToEnd.length());
        }

        String startyear = year[0].trim();
        startyear = startyear.substring(startyear.length() - 4, startyear.length());
        String endyear = year[1].trim();
        endyear = endyear.substring(endyear.length() - 4, endyear.length());

        if (!startyear.equals(Constants.EMPTYSPACE)) {
            startYear = Integer.parseInt(startyear);
        }
        if (!endyear.equals(Constants.EMPTYSPACE)) {
            endYear = Integer.parseInt(endyear);
        }
        yearList.add(startYear);
        yearList.add(endYear);
        return yearList;
    }

    private List<Integer> extractYears(String yearOnly, int startIndex, int endIndex) {
        List<Integer> yearList = new ArrayList<>();
        String yearStrToEnd = null;
        if (null != yearOnly) {
            yearStrToEnd = yearOnly.substring(startIndex + 1, endIndex);
        }
        if (null != yearStrToEnd && !yearStrToEnd.trim().equals(Constants.EMPTYSPACE)) {
            yearList = extractYearsUtil(yearStrToEnd);
        }
        return yearList;
    }

    private List<Timestamp> getStartAndEndDate(int startYear, int endYear) throws ParseException {
        List<Timestamp> timeList = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat(Constants.YEAR_PATTERN);
        Date dateStart = dateFormat.parse(String.valueOf(startYear));
        Date dateEnd = dateFormat.parse(String.valueOf(endYear));
        long timeStart = dateStart.getTime();
        long timeEnd = dateEnd.getTime();
        Timestamp timestampStart = new Timestamp(timeStart);
        Timestamp timestampEnd = new Timestamp(timeEnd);
        timeList.add(timestampStart);
        timeList.add(timestampEnd);
        return timeList;
    }

    private void onlyCollegeNameIsGiven(String[] educationString,
                                        List<CandidateEducationDetail> listofGetEducation) throws ParseException {
        CandidateEducationDetail candidateEducationDetail = new CandidateEducationDetail();
        CandidateSchoolDetail candidateSchoolDetail = new CandidateSchoolDetail();
        candidateEducationDetail.setEducation(Constants.DEFAULT_EDUCATION);
        String clgYear;
        clgYear = educationString[0];

        int startYear = 0;
        int endYear = 0;
        String regex = Constants.REGEX_PATTERN;
        String yearOnly = null;
        int startIndex = 0;
        int endIndex = 0;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clgYear);
        if (matcher.find()) {
            clgYear = clgYear.substring(0, matcher.start());
            yearOnly = matcher.group();
            startIndex = yearOnly.indexOf('(');
            endIndex = yearOnly.indexOf(')');
        }

        if (endIndex - startIndex > 8) {
            List<Integer> yearList = extractYears(yearOnly, startIndex, endIndex);
            startYear = yearList.get(0);
            endYear = yearList.get(1);
        }

        if (startYear == 0 && endYear == 0) {
            candidateSchoolDetail.setSchool(clgYear.replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateEducationDetail.setSchool(candidateSchoolDetail);
            listofGetEducation.add(candidateEducationDetail);
        } else {
            candidateSchoolDetail.setSchool(clgYear.replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            List<Timestamp> timeList = getStartAndEndDate(startYear, endYear);
            Timestamp timestampStart = timeList.get(0);
            Timestamp timestampEnd = timeList.get(1);
            candidateEducationDetail.setStartDate(timestampStart);
            candidateEducationDetail.setEndDate(timestampEnd);
            candidateEducationDetail.setSchool(candidateSchoolDetail);
            listofGetEducation.add(candidateEducationDetail);
        }
    }

    private void onlyCollegeNameAndCityAndDegreeGiven(String[] educationString,
                                                      List<CandidateEducationDetail> listofGetEducation)
                                                                                    throws ParseException {
        CandidateEducationDetail candidateEducationDetail = new CandidateEducationDetail();
        CandidateSchoolDetail candidateSchoolDetail = new CandidateSchoolDetail();
        CountryCityDetail countryCityDetail = new CountryCityDetail();
        candidateEducationDetail.setEducation(Constants.DEFAULT_EDUCATION);
        String clgYear;
        clgYear = educationString[2];

        int startYear = 0;
        int endYear = 0;
        String regex = Constants.REGEX_PATTERN;
        int startIndex = 0;
        int endIndex = 0;
        String yearOnly = null;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clgYear);
        if (matcher.find()) {
            clgYear = clgYear.substring(0, matcher.start());
            yearOnly = matcher.group();
            startIndex = yearOnly.indexOf('(');
            endIndex = yearOnly.indexOf(')');
        }

        if (endIndex - startIndex > 8) {
            List<Integer> yearList = extractYears(yearOnly, startIndex, endIndex);
            startYear = yearList.get(0);
            endYear = yearList.get(1);
        }

        if (clgYear.contains("(")) {
            clgYear = clgYear.substring(0, clgYear.indexOf('('));
        }

        if (startYear == 0 && endYear == 0) {
            countryCityDetail.setCity(clgYear.replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateSchoolDetail.setSchool(educationString[1].replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateEducationDetail.setDegree(educationString[0].replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateSchoolDetail.setCountryCity(countryCityDetail);
            candidateEducationDetail.setSchool(candidateSchoolDetail);
            listofGetEducation.add(candidateEducationDetail);
        } else {
            countryCityDetail.setCity(clgYear.replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateSchoolDetail.setSchool(educationString[1].replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateEducationDetail.setDegree(educationString[0].replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            List<Timestamp> timeList = getStartAndEndDate(startYear, endYear);
            Timestamp timestampStart = timeList.get(0);
            Timestamp timestampEnd = timeList.get(1);
            candidateEducationDetail.setStartDate(timestampStart);
            candidateEducationDetail.setEndDate(timestampEnd);
            candidateSchoolDetail.setCountryCity(countryCityDetail);
            candidateEducationDetail.setSchool(candidateSchoolDetail);
            listofGetEducation.add(candidateEducationDetail);
        }
    }


    private void onlyCollegeNameAndCityGiven(String[] educationString,
                                             List<CandidateEducationDetail> listofGetEducation) throws ParseException {
        CandidateEducationDetail candidateEducationDetail = new CandidateEducationDetail();
        CandidateSchoolDetail candidateSchoolDetail = new CandidateSchoolDetail();
        CountryCityDetail countryCityDetail = new CountryCityDetail();
        candidateEducationDetail.setEducation(Constants.DEFAULT_EDUCATION);
        String clgYear;
        clgYear = educationString[1];

        int startYear = 0;
        int endYear = 0;
        String regex = Constants.REGEX_PATTERN;
        int startIndex = 0;
        int endIndex = 0;
        String yearOnly = null;

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(clgYear);
        if (matcher.find()) {
            clgYear = clgYear.substring(0, matcher.start());
            yearOnly = matcher.group();
            startIndex = yearOnly.indexOf('(');
            endIndex = yearOnly.indexOf(')');
        }

        if (endIndex - startIndex > 8) {
            List<Integer> yearList = extractYears(yearOnly, startIndex, endIndex);
            startYear = yearList.get(0);
            endYear = yearList.get(1);
        }

        if (startYear == 0 && endYear == 0) {
            countryCityDetail.setCity(clgYear.replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateSchoolDetail.setSchool(educationString[0].replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateSchoolDetail.setCountryCity(countryCityDetail);
            candidateEducationDetail.setSchool(candidateSchoolDetail);
            listofGetEducation.add(candidateEducationDetail);
        } else {
            countryCityDetail.setCity(clgYear.replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateSchoolDetail.setSchool(educationString[0].replaceAll(Constants.REGEX_GENERAL, Constants.EMPTYSPACE));
            candidateSchoolDetail.setCountryCity(countryCityDetail);
            List<Timestamp> timeList = getStartAndEndDate(startYear, endYear);
            Timestamp timestampStart = timeList.get(0);
            Timestamp timestampEnd = timeList.get(1);
            candidateEducationDetail.setStartDate(timestampStart);
            candidateEducationDetail.setEndDate(timestampEnd);
            candidateEducationDetail.setSchool(candidateSchoolDetail);
            listofGetEducation.add(candidateEducationDetail);
        }
    }


    /**
     * getting the details from excel sheet
     *
     * @return
     * @throws IOException
     * @throws ParseException
     */
    public List<SetPojo> readXLSXFile() throws IOException, ParseException {
        InputStream excelFileToRead = new FileInputStream(excelPath);
        XSSFSheet sheet;
        XSSFRow row;
        XSSFCell cell;
        Map<String, Integer> map = new HashMap<>();
        int minColumnIndex;
        int maxColumnIndex;
        List<SetPojo> listOfDataFromReportSet;
        try (XSSFWorkbook wb = new XSSFWorkbook(excelFileToRead)) {
            sheet = wb.getSheetAt(0);
        }

        row = sheet.getRow(0);
        minColumnIndex = row.getFirstCellNum();
        maxColumnIndex = row.getLastCellNum();
        for (int colIndex = minColumnIndex; colIndex < maxColumnIndex; colIndex++) { //loop from first to last index
            cell = row.getCell(colIndex); //get the cell
            map.put(cell.getStringCellValue(), cell.getColumnIndex()); //add the cell contents (name of column) and cell index to the map
        }

        Map<String, Long> mapOfCountryCity = getCityCountryId();

        listOfDataFromReportSet = new ArrayList<>();

        for (int rowIndex = 1; rowIndex <= 26836; rowIndex++) {//instead of 10 put totalRows

            SetPojo setObject = new SetPojo();

            XSSFRow dataRow = sheet.getRow(rowIndex);

            readName(dataRow, map, setObject);
            readEmail(dataRow, map, setObject);
            readLinkedIn(dataRow, map, setObject);
            readExperience(dataRow, map, setObject);
            readCompany(dataRow, map, setObject);
            readSkills(dataRow, map, setObject);
            readLocation(dataRow, map, setObject, mapOfCountryCity);
            readEducation(dataRow, map, setObject);
            readApplication(setObject);

            listOfDataFromReportSet.add(setObject);
        }
        return listOfDataFromReportSet;
    }


    public static void main(String[] args) throws IOException, ParseException {
        ReadFromExcel readFromExcel = new ReadFromExcel();
        List<SetPojo> listofContent = readFromExcel.readXLSXFile();
        readFromExcel.writeJsonUsingObjectMapper(listofContent);
    }
}


