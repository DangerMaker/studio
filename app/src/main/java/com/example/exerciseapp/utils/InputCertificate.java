package com.example.exerciseapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputCertificate {

	public static boolean isPhoneNum(String phoneNum){  

		Pattern p = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");  
		Matcher m = p.matcher(phoneNum);  
		return m.matches();  
	}  
	
	public static boolean isPassword(String password){
		Pattern p = Pattern.compile("^[a-zA-Z0-9_]\\w{3,9}+$");  
		Matcher m = p.matcher(password);  
		return m.matches();  
	}
}
