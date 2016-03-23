package com.example.exerciseapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckInput {

	
	//是否是电话号码
	public static boolean isPhoneNum(String phoneNum){  

		Pattern p = Pattern.compile("^1[3|4|5|7|8]\\d{9}$");  
		Matcher m = p.matcher(phoneNum);  
		return m.matches();  
	}  
	
	
	//是否是密码4-10位英文，数字下划线结合
	public static boolean isPassword(String password){
		Pattern p = Pattern.compile("^[a-zA-Z0-9_]\\w{3,9}+$");  
		Matcher m = p.matcher(password);  
		return m.matches();  
	}
	
	//是否是身份证
	public static boolean isIDCard(String IDCard){
		 Pattern idNumPattern = Pattern.compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");  
         //通过Pattern获得Matcher  
         Matcher idNumMatcher = idNumPattern.matcher(IDCard);
         return idNumMatcher.matches();
	}
	
	//性别输入正常
	public static boolean isSexInput(String sex){
		if(sex.equals("男")||sex.equals("女")){
			return true;
		}
		return false;
	}
	
	//血型输入检验
	public static boolean isBloodType(String blood){
		if(blood.equals("")||
				blood.toUpperCase().equals("A")||
				blood.toUpperCase().equals("B")||
				blood.toUpperCase().equals("AB")||
				blood.toUpperCase().equals("O")){
				return true;
			
		}
		return false;
	}
	
	//所属协会/协会单位输入验证
	public static boolean isBelongedAssocAndAssocUnit(String input){
		if(input.indexOf("/") == -1){
			return false;
		}
		return true;
	}
	
	
	
}
