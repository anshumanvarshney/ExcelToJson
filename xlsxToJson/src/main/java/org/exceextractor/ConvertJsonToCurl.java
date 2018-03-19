package org.exceextractor;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import java.io.*;

/**
 * Created by Anshuman Varshney on 3/3/18.
 */
public class ConvertJsonToCurl {

    @Value("${json.path}")
    private String jsonPath;

    @Value("${curl.path}")
    private String curlPath;

    @Value("${excel.path}")
    private String excelPath;

//    static Logger log = Logger.getLogger(ConvertJsonToCurl.class.getName());

    /**
     * return content of file in a string
     *
     * @param index
     * @return
     */
    public  String getContentFromFile(int index) {

        StringBuilder middle = new StringBuilder();
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(jsonPath + index);
        } catch (FileNotFoundException e) {
         //   log.error(Constants.ERROR,e);
        }

        try {
            try (BufferedReader buf = new BufferedReader(new InputStreamReader(inputStream))) {
                String line = buf.readLine();
                while (line != null) {
                    middle.append(line).append("\n");
                    line = buf.readLine();
                }
            }
        } catch (IOException e) {
         //   log.error(Constants.ERROR,e);
        }
        return middle.toString();
    }

    /**
     * making of curl files from Json files
     */
    public void makeCurlFiles() {
        String middle = null;
        String fileAsString = null;

        for (int index = 0; index < 26836; index++) {
            StringBuilder content = new StringBuilder();
            middle = getContentFromFile(index);
            content.append(Constants.CURL_PART);
            fileAsString = middle;
            content.append(fileAsString);
            content.append(Constants.FINAL_PART);
            String fileName = curlPath;
            try (PrintWriter out = new PrintWriter(fileName+ index + Constants.EXTENSION)) {
                out.println(content.toString());
            } catch (FileNotFoundException e) {
                //log.error(Constants.ERROR,e);
            }

        }
    }

    public static void main(String[] args) {
        ConvertJsonToCurl convertJsonToCurl = new ConvertJsonToCurl();
        convertJsonToCurl.makeCurlFiles();
    }
}

