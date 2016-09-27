package com.example.exerciseapp.aty.organzie.mutipick;

import java.io.Serializable;

/**
 * 一个图片对象
 * 
 * Created by shand on 2016/4/27.
 * 
 */
public class ImageItem implements Serializable{
	public String imageId;
	public String thumbnailPath;
	public String imagePath;
	public boolean isSelected = false;

}
