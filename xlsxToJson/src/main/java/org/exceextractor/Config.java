package org.exceextractor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.regex.Pattern;

@Configuration
public class Config {

	@Autowired
	private Environment env;

	private String getJsonPath(){
		return "/Users/anshumanvarshney/Downloads/jsfiles/";
	}

	private String getCurlPath(){
		return "/Users/anshumanvarshney/Downloads/curlSh/";
	}

	private String getExcelPath(){
		return "/Users/anshumanvarshney/Downloads/Done-2018-02-20-MiscComp.xlsx";
	}
}
