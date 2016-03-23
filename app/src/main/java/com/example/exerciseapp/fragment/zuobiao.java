package com.example.exerciseapp.fragment;


public class zuobiao {
	public double jingdu;
	public double weidu;
	public zuobiao(){}
	public zuobiao(double x,double y){
		this.jingdu=x;
		this.weidu=y;
	}
	
	public void zuoBiaoAdd(){
		this.jingdu+=0.00001;
		this.weidu +=0.00002;
	}
	

}