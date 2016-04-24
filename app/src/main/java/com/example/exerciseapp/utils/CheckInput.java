package com.example.exerciseapp.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckInput {

	/**
	 * 正则表达式：验证邮箱
	 */
	public static final String REGEX_EMAIL = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";

	/**
	 * 校验邮箱
	 *
	 * @param email
	 * @return 校验通过返回true，否则返回false
	 */
	public static boolean isEmail(String email) {
		return Pattern.matches(REGEX_EMAIL, email);
	}

	//是否是电话号码
	public static boolean isPhoneNum(String phoneNum){  

		Pattern p = Pattern.compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");
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
		 Pattern idNumPattern = Pattern.compile("(^\\d{18}$)|(^\\d{15}$)");
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
