package com.example.exerciseapp;

/**
 * 配置函数
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;

public class Config {
	
	public static boolean TOURIST_MODE = false;
	
	public static ACache mCache = null;
	//缓存时间，7天
	public static final int CACHE_TIME = 7 * ACache.TIME_DAY;
	
	//app-tag
	public static final String APP_TAG = "ExerciseApp";
	//服务器地址
	public static final String SERVER_URL = "http://101.200.214.68/index.php/Api/";
	//WEIXIN_APP_ID
	public static final String WxAPP_ID = "wxc12890676f5f2319";
	//通信方式
	public static final String POST = "POST";
	public static final String GET = "GET";
	
	//描述（desc）编码格式
	public static final String CHARSET = "utf-8";
	
	//左侧菜单栏点击对应的页面
//	public static final int PAGE_TAG_COMPETETION_ACTIVIES = 0;	
//	public static final int PAGE_TAG_START_RUNNING = 1;	
//	public static final int PAGE_TAG_MY_LIST = 2;	
//	public static final int PAGE_TAG_CLUB = 3;	
//	public static final int PAGE_TAG_PERSONAL_CENTER = 4;
//	public static final int PAGE_TAG_NEWS = 5;
//	public static final int PAGE_TAG_CONFIG = 6;
//	public static final int PAGE_USER_INFORMATION_HOME = 7;	
	public static final int PAGE_TAG_COMPETETION_ACTIVIES = 0;	
	public static final int PAGE_TAG_START_RUNNING = 1;	
	public static final int PAGE_TAG_CLUB = 2;	
	public static final int PAGE_TAG_PERSONAL_CENTER = 3;
	public static final int PAGE_TAG_NEWS = 4;
	public static final int PAGE_TAG_CONFIG = 5;
	public static final int PAGE_USER_INFORMATION_HOME = 6;
	public static final int PAGE_TAG_TEAM = 7;
	
	
	//通信所用的键
	public static final String KEY_UID = "uid";					//用户ID
	public static final String KEY_USER_NAME = "username";		//用户名
	public static final String KEY_NICK_NAME = "nickname";		//昵称
	public static final String KEY_USER_BIRTHDAY = "birthday";	//出生日期 （xxxx-xx-xx)
	public static final String KEY_TEL = "tel";					//用户电话
	public static final String KEY_TZ = "tz";					//体脂
	public static final String KEY_BMI = "bmi";					//身体质量指数（正常 肥胖 非常肥胖 等）
	public static final String KEY_AVATAR = "avatar";			//用户头像存储url
	public static final String KEY_PASSWORD = "password";		//用户密码
	public static final String KEY_ORI_PASSWORD = "ori_password";		//用户修改密码原始密码
	public static final String KEY_NEW_PASSWORD = "new_password";		//用户修改密码新密码
	public static final String KEY_CODE = "code";				//验证码
	public static final String KEY_SEX = "sex";					//用户性别	
	public static final String KEY_AGE = "age";					//用户年龄
	public static final String KEY_HEIGHT = "height";			//用户身高
	public static final String KEY_WEIGHT = "weight";			//用户体重
	public static final String KEY_AREA = "area";				//所在地
	public static final String KEY_BLOOD = "blood";				//血型
	public static final String KEY_USER_HW = "fileurl";			//
	public static final String KEY_ID_CARD = "idcard";			//idcard 
	public static final String KEY_EMAIL= "email";				//email 
	public static final String KEY_TEL_ADDRESS = "tel_address";	//通信地址
	public static final String KEY_ZIP_CODE = "zipcode";		//邮编
	public static final String KEY_COUNTRY = "country";			//国家
	public static final String KEY_APOSITION = "aposition";	//协会所属地区
	public static final String KEY_ASSOC = "assoc";				//用户所处协会名称
	public static final String KEY_EMER_NAME = "emer_name";		//紧急联系人姓名
	public static final String KEY_EMER_TEL = "emer_tel";		//紧急联系人电话
	public static final String KEY_INTEREST = "interest";		//健身项目id组合
	public static final String KEY_INTEREST_NAME = "iname";		//健身项目名称
	
	
	public static final String KEY_GAME_ID = "gid";				//赛事id
	public static final String KEY_GAME_H5_URL = "gintro";				//赛事id
	public static final String KEY_GAME_LIST = "gameList";		//赛事列表
	public static final String KEY_GAME_NAME = "gname";			//赛事名称
	public static final String KEY_GAME_TYPE = "gtype";			//赛事类型（如跑步）
	public static final String KEY_GAME_LEVEL = "glevel";		//赛事级别（例如国家级）
	public static final String KEY_GAME_STATUS = "gstatus";		//赛事状态（例如接受报名）
	public static final String KEY_GAME_STATUS_ID = "gstatusid";//赛事状态id(与gstatus相对应）
	public static final String KEY_GAME_ASSOC = "gassoc";		//举办赛事的协会 
	public static final String KEY_GAME_POSITION = "gposition";	//赛事举办地点
	public static final String KEY_GAME_START_TIME = "gstarttime";//赛事开始时间
	public static final String KEY_GAME_END_TIME = "gendtime";	//赛事结束时间
	public static final String KEY_GAME_ACCEPT_START = "gacceptstart";//赛事接受报名时间
	public static final String KEY_GAME_ACCEPT_START_NEW = "gacceptstartdate";//赛事接受报名时间 new
	public static final String KEY_GAME_ACCEPT_END = "gacceptend";//赛事截止报名时间
	public static final String KEY_GAME_ACCEPT_END_NEW = "gacceptenddate";//赛事截止报名时间 new
	public static final String KEY_GAME_OUT_TIME = "gouttime";	//赛事发布时间
	public static final String KEY_GAME_MAX_ATTEND = "gmaxattend";//赛事最大报名人数
	public static final String KEY_GAME_ATTEND = "gattend";//赛事报名人数
	public static final String KEY_GAME_BIREF_INTRO = "gbriefintro";//赛事简介（文字）
	public static final String KEY_GAME_INTRO = "gintro";		//赛事简介（图片url )
	public static final String KEY_GAME_FRONT_PAGE = "gfrontpage";//赛事封面图（为图片url）
	public static final String KEY_GAME_NEED_PAY = "needpay";	//是否需要支付（付费 or 免费）
	public static final String KEY_GAME_PAY_FEE = "gpayfee";	//赛事报名费用
	public static final String KEY_GAME_SPONSER = "gsponsor";	//赛事主办协会
	public static final String KEY_GAME_ORGANIZER = "gorganizer";	//赛事承办协会
	public static final String KEY_GAME_COUNT_NOW = "countnow";	//已报名人数
	
	public static final String KEY_FRONT_PAGE = "frontpage";
	public static final String KEY_UEID = "ueid";				//报名信息Id
	public static final String KEY_ATTEND_TIME = "attendtime";	//申请表提交时间
	public static final String KEY_CHECK_STATUS = "checkstatus";//申请参加的赛事状态标志
	public static final String KEY_CHECK_STATUS_NAME = "statusname";//申请参加的赛事状态信息
	public static final String KEY_ATTEND_PERSON = "username";		//参赛人员
	
	public static final String KEY_FILE_URL = "fileurl";			//头像URL
	
	//申请加入赛事的状态
	public static final String CHECK_STATUS_NO_PAY = "0";		//未支付
	public static final String CHECK_STATUS_IN_CHECK = "1";		//未支付
	public static final String CHECK_STATUS_SUCCESS = "2";		//未支付
	public static final String CHECK_STATUS_FAILED = "3";		//未支付
	
	//修改赛事报名资料
	public static final String KEY_EUSERNAME = "eusername";		//报名人姓名
	public static final String KEY_ESEX = "esex";			//报名人性别
	public static final String KEY_EAGE = "eage";			//报名人年龄
	public static final String KEY_ETEL = "etel";			//报名人联系方式
	public static final String KEY_EIDCARD = "eidcard";		//报名人身份证号
	
	
	//我的成绩
	public static final String KEY_ESCORE = "escore";		//赛事项目成绩
	public static final String KEY_TITLE = "title";
	public static final String KEY_DATE = "date";		
	
	public static final String KEY_USER_INFO = "userInfo";		//用户信息
	public static final String KEY_EVENT_INFO = "eventInfo";	//赛事信息
	
	public static final String KEY_USER_ATTEND_EID = "eid";		//赛事项目id
	public static final String KEY_USER_ATTEND_ENAME = "ename";		//赛事项目名字
	public static final String KEY_USER_ATTEND_EATTEND = "eattend";		//赛事项目已参加人数
	public static final String KEY_USER_ATTEND_EPAYFEE= "epayfee";		//赛事项目付费
	public static final String KEY_USER_ATTEND_EMAXATTEND = "emaxattend";//赛事项目最大参加人数
	public static final String KEY_USER_ATTEND_USER_NAME = "eusername";//提交姓名
	public static final String KEY_USER_ATTEND_TEL = "etel";		//提交手机
	public static final String KEY_USER_ATTEND_SEX = "esex";		//性别(男or女）
	public static final String KEY_USER_ATTEND_IDCARD = "eidcard";	//身份证
	public static final String KEY_USER_ATTEND_AGE = "eage";		//年龄
	public static final String KEY_ADVICE_NEW_CONTENT = "content";	//反馈内容
	public static final String KEY_MY_AVATAR = "myAvatar";	//用户选择的头像图片

	
	//我的协会俱乐部
	public static final String KEY_USER_AREA = "userarea";		//用户所处地区
	public static final String KEY_ANAME = "aname";				//俱乐部名字
	public static final String KEY_AID = "aid";				//俱乐部ID
	public static final String KEY_ID = "id";				//编号ID
	public static final String KEY_APAY_FEE = "apayfee";	//加入协会的费用
	public static final String KEY_ASSOC_INFO = "info";
	
	//******************项目，级别，费用点击Flag*******************************
	public static final int FLAG_GAME_PROJECT_DEFAULT = 0;
	public static final int FLAG_GAME_PROJECT_BALL = 1;				//选中球类
	public static final int FLAG_GAME_PROJECT_RUN = 2;				//选中跑步
	public static final int FLAG_GAME_PROJECT_ICE = 3;				//选中冰水
	public static final int FLAG_GAME_PROJECT_BICYCLE = 4;			//选中自行车
	public static final int FLAG_GAME_PROJECT_PUBLIC = 5;			//选中大众
	public static final int FLAG_GAME_PROJECT_EXTRA = 6;			//其它
	
	public static final int FLAG_GAME_LEVEL_DEFAULT = 0;			//默认级别
	public static final int FLAG_GAME_LEVEL_INTERNATIONAL = 1;		//国际级
	public static final int FLAG_GAME_LEVEL_NATIONAL = 2;			//国家级
	public static final int FLAG_GAME_LEVEL_PROVINCE = 3;			//省级
	public static final int FLAG_GAME_LEVEL_CITY = 4;				//市级
	public static final int FLAG_GAME_LEVEL_AREA = 5;				//区级
	
	public static final int FLAG_GAME_PAY_DEFAULT = 0;				//
	public static final int FLAG_GAME_PAY_FREE = 1;					//免费
	public static final int FLAG_GAME_PAY_NEED_PAY = 2;				//付费
		
	//************************赛事列表筛选条目**************************
	public static final String BALL = "球类";
	public static final String RUN = "跑步";
	public static final String DEFAULT_PROJECT = "项目";
	public static final String ICEWATER = "冰水";
	public static final String BICYCLE = "自行车";
	public static final String PUBLIC = "大众运动";
	public static final String EXTRA = "其他";
	
	public static final String DEFAULT_LEVEL = "级别";
	public static final String INTERNATIONAL = "国际";
	public static final String NATIONAL = "国家级";
	public static final String PROVINCE = "省级";
	public static final String CITY = "市级";
	public static final String AREA = "区县";
	
	public static final String DEFAULT_FEE = "费用";
	public static final String FREE_PAY = "免费";
	public static final String NEED_PAY = "付费";
	private static final String KEY_MY_ENTRY_FORM_LIST = "myentryformlist";
	private static final String KEY_BRIEF_USER_INFORMATION = "briefUserInformation";
	private static final String KEY_AVATAR_BITMAP = "bitmap";
	public static final String KEY_MEMBER_NUM = "member_num";
	public static final String KEY_AINTRO = "aintro";
	public static final String KEY_OPENTYPE = "opentype";
	private static final String KEY_ASSOC_LIST = "assoclist";
	private static final String KEY_NEWS_LIST = "newslist";
	
	
	public static final String KEY_DURATION="duration";      //
	public static final String KEY_AVERAGESPEED="averagespeed";   
	public static final String KEY_CALORIE="calorie";           //鍗¤矾閲�   鍗�
	public static final String KEY_DISTANCE="distance";      //璺濈  km
	public static final String KEY_REMARK="remark";         //澶囨敞
	
	//现场实况
	public static final String KEY_PIC_NUM = "picNum";
	public static final String KEY_POSITION = "position";
	public static final String KEY_PIC_LIST = "picList";
	public static final String KEY_CONTENT = "content";
	public static final String KEY_TIME = "time";
	public static final String KEY_PIC = "pic";
	
	public static String aId = null;
	public static boolean SHOW_GAME_LIST_FRAGMENT = false;
	public static boolean SHOW_NEWS_FRAGMENT = false;
	
	public static boolean STATUS_HAS_FOCUSED = false;		
	public static boolean STATUS_HAS_MESSAGE = false;		
	public static boolean STATUS_PUSH = false;
	public static boolean STATUS_SUBMIT_USER_HW = false;
	
	
	public static String CONNECTION_ERROR = "网络连接错误";		//错误提示
	public static String AGREE_RULE = "已阅读并同意规程";
	//***************************************************************
	
	public static Bitmap bitMap;
	
	
	public static boolean IS_FULL_SCREEN = true;
	
	public static int STATUS_FINISH_ACTIVITY = 0;
	public static int CAN_MODIFY_ALL_INFORMATION = 1;	//是否修改所有资料，默认为可以
	
	public static String bitmapString;
	
	//地區json解析
	public static List<Map<String,String>> cityList = new LinkedList<Map<String,String>>();
	public static List<Map<String,Map<String,String>>> cityAreaList = new LinkedList<Map<String,Map<String,String>>>();
	
	//当前点击赛事
	public static String currentClickedGameItem = null;
	
	
	
	//缓存用户uid
	public static void cacheUserUid(Context context, String uid){
		mCache = ACache.get(context,"Uid");
		if(mCache.getAsString(KEY_UID) != null){
			mCache.remove(KEY_UID);
		}
		mCache.put(KEY_UID, uid, CACHE_TIME);
	}
	
	
	public static String getCachedUserUid(Context context){
		mCache = ACache.get(context,"Uid");
		return mCache.getAsString(KEY_UID);
	}

	//缓存用户tel
	public static void cacheUserTel(Context context, String tel){
		mCache = ACache.get(context,"tel");
		if(mCache.getAsString(KEY_TEL) != null){
			mCache.remove(KEY_TEL);
		}
		mCache.put(KEY_TEL, tel);
	}

	public static String getCachedUserTel(Context context){
		mCache = ACache.get(context,"tel");
		return mCache.getAsString(KEY_TEL);
	}
	
	//缓存用户名和生日
	public static void cacheUserName(Context context, String userName,String userBirthday){
		mCache = ACache.get(context,"UserName");
		if(mCache.getAsString(KEY_USER_NAME)!= null){
			mCache.remove(KEY_USER_NAME);
		}
		if(mCache.getAsString(KEY_USER_BIRTHDAY) == null){
			mCache.remove(KEY_USER_BIRTHDAY);
		}
		mCache.put(KEY_USER_NAME, userName, CACHE_TIME);
		mCache.put(KEY_USER_BIRTHDAY, userBirthday, CACHE_TIME);
	}
	
	public static String[] getCachedUserName(Context context){
		mCache = ACache.get(context,"UserName");
		return new String[]{mCache.getAsString(KEY_USER_NAME),mCache.getAsString(KEY_USER_BIRTHDAY)};
	}
	
	
	//清除所有缓存
	public static void clearAllCache(Context context){
		mCache = ACache.get(context);
		String uid = mCache.getAsString(KEY_UID);
		mCache.clear();
		mCache.put(KEY_UID, uid);
	}
	
	//缓存用户信息
	public static void cacheBriefUserInformation(Context context,JSONObject jsonObj){
		mCache = ACache.get(context,"BriefUserInformation");
		if(mCache.getAsString(KEY_BRIEF_USER_INFORMATION)!=null){
			mCache.remove(Config.KEY_BRIEF_USER_INFORMATION);
		}
		mCache.put(Config.KEY_BRIEF_USER_INFORMATION, jsonObj);
	}

	public static JSONObject getCachedBriefUserInformation(Context context){
		//TODO
		mCache = ACache.get(context,"BriefUserInformation");
		if(mCache==null){
			return null;
		}
		return mCache.getAsJSONObject(Config.KEY_BRIEF_USER_INFORMATION);
	}
	
	
	//缓存用户信息
	public static void cacheUserInformation(Context context, JSONObject jsonObj){
			mCache = ACache.get(context,"UserInformation");
			if(mCache.getAsJSONObject(Config.KEY_USER_INFO)!=null){
				mCache.remove(Config.KEY_USER_INFO);
			}
			mCache.put(Config.KEY_USER_INFO, jsonObj);
			//TODO 单独缓存身高体重
	}
	
	public static JSONObject getCachedUserInformation(Context context){
		//TODO
		mCache = ACache.get(context,"UserInformation");
		return mCache.getAsJSONObject(Config.KEY_USER_INFO);
	}
	//缓存身高体重
	public static void cacheHeightAndWeight(Context context,String height,String weight){
		mCache = ACache.get(context,"WeightAndHeight");
		if(mCache.getAsString(Config.KEY_HEIGHT)!=null){
			mCache.remove(Config.KEY_HEIGHT);
		}
		if(mCache.getAsString(Config.KEY_WEIGHT)!=null){
			mCache.remove(Config.KEY_WEIGHT);
		}
		mCache.put(Config.KEY_HEIGHT, height);
		mCache.put(Config.KEY_WEIGHT, weight);
		
	}
	
	public static String getCachedHeigh(Context context){
		mCache = ACache.get(context,"Weight");
		return mCache.getAsString(Config.KEY_HEIGHT);
	}
	public static String getCachedWeight(Context context){
		mCache = ACache.get(context,"Weight");
		return mCache.getAsString(Config.KEY_WEIGHT);
	}
	

	//缓存头像链接
	public static void cacheUserHwURL(Context context,String url){
		mCache = ACache.get(context,"UserHwURL");
		if(mCache.getAsString(KEY_AVATAR)!= null){
			mCache.remove(KEY_AVATAR);
		}
		mCache.put(KEY_AVATAR, url, CACHE_TIME);
	}
	
	public static String getCachedUserHwURL(Context context){
		mCache = ACache.get(context,"UserHwURL");
		if(mCache == null){
			return null;
		}
		return mCache.getAsString(KEY_AVATAR);
	}
	
	//缓存头像
	public static void cacheUserHw(Context context,Bitmap bitmap){
		mCache = ACache.get(context,"UserHw");
		if(mCache.getAsBitmap(KEY_AVATAR_BITMAP)!= null){
			mCache.remove(KEY_AVATAR_BITMAP);
		}
		mCache.put(KEY_AVATAR_BITMAP, bitmap, CACHE_TIME);
	}
	
	public static Bitmap getCachedUserHw(Context context){
		mCache = ACache.get(context,"UserHw");
		return mCache.getAsBitmap(KEY_AVATAR_BITMAP);
	}
	
	//缓存活动front图片
	public static void cacheGameFrontPage(Context context,Bitmap bitmap,String gid){
		mCache = ACache.get(context,"GameFrontPage");
		if(mCache.getAsBitmap(gid+"FrontPage") != null){
			mCache.remove(gid+"FrontPage");
		}
		mCache.put(gid+"FrontPage",bitmap);
	}
	public static Bitmap getCachedFrontPage(Context context,String gid){
		mCache = ACache.get(context,"GameFrontPage");
		return mCache.getAsBitmap(gid+"FrontPage");
	}
	//缓存活动介绍图片
	public static void cacheGameIntroPage(Context context,Bitmap bitmap,String gid){
		mCache = ACache.get(context,"GameIntroPage");
		if(mCache.getAsBitmap(gid+"IntroPage") != null){
			mCache.remove(gid+"IntroPage");
		}
		mCache.put(gid+"IntroPage",bitmap);
	}
	public static Bitmap getCachedIntroPage(Context context,String gid){
		mCache = ACache.get(context,"GameIntroPage");
		return mCache.getAsBitmap(gid+"IntroPage");
	}
	
	
	public static void cacheCurrentClickingGameId(Context context, String gid){
		mCache = ACache.get(context,"currentClickingGameId");
		if(mCache.getAsString(KEY_GAME_ID)!= null){
			mCache.remove(KEY_GAME_ID);
		}
		mCache.put(KEY_GAME_ID, gid, CACHE_TIME);
	}
	public static String getCachedCurrentClickedGameId(Context context){
		mCache = ACache.get(context,"currentClickingGameId");
		return mCache.getAsString(KEY_GAME_ID);
	}
	
	
	//缓存赛事信息列表
	public static void cacheGameList(Context context, JSONArray jsonArr) throws JSONException{
		mCache = ACache.get(context,"GameList");
		if(mCache.getAsJSONArray(KEY_GAME_LIST) !=null){
//			JSONArray jsonArrOld = mCache.getAsJSONArray(KEY_GAME_LIST);
//			for (int i = 0; i < jsonArr.length(); i++) {
//				if(!isExistedJsonObjectInJsonArray(null,context, jsonArr.getJSONObject(i).getString(KEY_GAME_ID))){
//					jsonArrOld.put(jsonArr.getJSONObject(i));
//				}
//			}
			mCache.remove(KEY_GAME_LIST);
			mCache.put(KEY_GAME_LIST, jsonArr,CACHE_TIME);
		}else{
			mCache.put(KEY_GAME_LIST, jsonArr,CACHE_TIME);
		}
	}
	
	public static JSONArray getCachedGameList(Context context){
		mCache = ACache.get(context,"GameList");
		return mCache.getAsJSONArray(KEY_GAME_LIST);
	}
	
	public static void cacheMessage(Context context,JSONArray jsonArr){
		mCache = ACache.get(context,"Message");
		if(mCache.getAsJSONArray("Message") !=null){
			mCache.remove("Message");
			mCache.put("Message", jsonArr,CACHE_TIME);
		}else{
			mCache.put("Message", jsonArr,CACHE_TIME);
		}
	}
	
	public static JSONArray getCachedMessage(Context context){
		return ACache.get(context,"Message").getAsJSONArray("Message");
	}
	
	//缓存协会信息列表
		public static void cacheAssocList(Context context, JSONArray jsonArr) throws JSONException{
			mCache = ACache.get(context,"AssocList");
			if(mCache.getAsJSONArray(KEY_ASSOC_LIST) !=null){
//				JSONArray jsonArrOld = mCache.getAsJSONArray(KEY_ASSOC_LIST);
//				for (int i = 0; i < jsonArr.length(); i++) {
//					if(!isExistedJsonObjectInJsonArrayAssocList(context, jsonArr.getJSONObject(i).getString(KEY_AID))){
//						jsonArrOld.put(jsonArr.getJSONObject(i));
//					}
//				}
				mCache.remove(KEY_ASSOC_LIST);
				mCache.put(KEY_ASSOC_LIST, jsonArr,CACHE_TIME);
			}else{
				mCache.put(KEY_ASSOC_LIST, jsonArr,CACHE_TIME);
			}
		}
		
		public static JSONArray getCachedAssocList(Context context){
			mCache = ACache.get(context,"AssocList");
			return mCache.getAsJSONArray(KEY_ASSOC_LIST);
		}
		//缓存新闻列表
		public static void cacheNewsList(Context context, JSONArray jsonArr) throws JSONException{
			mCache = ACache.get(context,"NewsList");
			if(mCache.getAsJSONArray(KEY_NEWS_LIST) !=null){
//				JSONArray jsonArrOld = mCache.getAsJSONArray(KEY_NEWS_LIST);
//				for (int i = 0; i < jsonArr.length(); i++) {
//					if(!isExistedJsonObjectInJsonArrayNewsList(context, jsonArr.getJSONObject(i).getString(KEY_ID))){
//						jsonArrOld.put(jsonArr.getJSONObject(i));
//					}
//				}
				mCache.remove(KEY_NEWS_LIST);
				mCache.put(KEY_NEWS_LIST, jsonArr,CACHE_TIME);
			}else{
				mCache.put(KEY_NEWS_LIST, jsonArr,CACHE_TIME);
			}
		}
		
		public static JSONArray getCachedNewsList(Context context){
			mCache = ACache.get(context,"NewsList");
			return mCache.getAsJSONArray(KEY_NEWS_LIST);
		}
	
	public static JSONObject getJsonObjectFromJsonArray(Context context, String gid) throws JSONException{
		JSONArray jsonArr = getCachedGameList(context);
		for (int i = 0; i < jsonArr.length(); i++) {
			if(jsonArr.getJSONObject(i).getString(KEY_GAME_ID).equals(gid)){
				return jsonArr.getJSONObject(i);
			}
		}
		return null;
	}
	
	
	//缓存我的报名列表
		public static void cacheMyEntryFormList(Context context, JSONArray jsonArr) throws JSONException{
			mCache = ACache.get(context,"MyEntryFormList");
			if(mCache.getAsJSONArray(KEY_MY_ENTRY_FORM_LIST) !=null){
//				JSONArray jsonArrOld = mCache.getAsJSONArray(KEY_MY_ENTRY_FORM_LIST);
//				for (int i = 0; i < jsonArr.length(); i++) {
//					if(!isExistedJsonObjectInJsonArray(null,context, jsonArr.getJSONObject(i).getString(KEY_GAME_ID))){
//						jsonArrOld.put(jsonArr.getJSONObject(i));
//					}
//				}
				mCache.remove(KEY_MY_ENTRY_FORM_LIST);
				mCache.put(KEY_MY_ENTRY_FORM_LIST, jsonArr,CACHE_TIME);
			}else{
				mCache.put(KEY_MY_ENTRY_FORM_LIST, jsonArr,CACHE_TIME);
			}
		}
		
		public static JSONArray getCachedMyEntryFormList(Context context){
			mCache = ACache.get(context,"MyEntryFormList");
			return mCache.getAsJSONArray(KEY_MY_ENTRY_FORM_LIST);
		}
	
	
	//根据gid从缓存中取得赛事信息
	public JSONObject getJsonObjectFromCachedJsonArray(Context context,String gid) throws JSONException{
		if(getCachedGameList(context) == null){
			return null;
		}
		JSONArray jsonArr = getCachedGameList(context);
		for (int i = 0; i < jsonArr.length(); i++) {
			if(jsonArr.getJSONObject(i).getString(KEY_GAME_ID).equals(gid)){
				return jsonArr.getJSONObject(i);
			}
		}
		return null;
	}
	//是否存在具有gid的赛事JsonObject
	public static boolean isExistedJsonObjectInJsonArray(JSONArray jsonArr,Context context, String gid) throws JSONException{
		if(getCachedGameList(context) == null){
			return false;
		}
		if(jsonArr == null){
			jsonArr = getCachedGameList(context);
		}
		for (int i = 0; i < jsonArr.length(); i++) {
			if(jsonArr.getJSONObject(i).getString(KEY_GAME_ID).equals(gid)){
				return true;
			}
		}
		return false;
	}
	
	//是否存在具有gid的赛事JsonObject
		public static boolean isExistedJsonObjectInJsonArrayAssocList(Context context, String aid) throws JSONException{
			if(getCachedAssocList(context) == null){
				return false;
			}
			JSONArray jsonArr = getCachedAssocList(context);
			for (int i = 0; i < jsonArr.length(); i++) {
				if(jsonArr.getJSONObject(i).getString(KEY_AID).equals(aid)){
					return true;
				}
			}
			return false;
		}
		//是否存在具有gid的赛事JsonObject
		public static boolean isExistedJsonObjectInJsonArrayNewsList(Context context, String id) throws JSONException{
			if(getCachedNewsList(context) == null){
				return false;
			}
			JSONArray jsonArr = getCachedNewsList(context);
			for (int i = 0; i < jsonArr.length(); i++) {
				if(jsonArr.getJSONObject(i).getString(KEY_ID).equals(id)){
					return true;
				}
			}
			return false;
		}
	
	//退出当前账号
	public static void quitCurrentUser(Context context){
		mCache = ACache.get(context,getCachedUserUid(context));
		mCache.remove(KEY_UID);
		clearAllCache(context);
		
	}
	
	
	//缓存地址数据库
	public static void cacheCity(Context context){
		mCache = ACache.get(context,"City");
		//TODO
		if(getJson(context,"beijing.json").length()==0){
			return;
		}
		try {
			JSONObject jsonArr = new JSONObject(getJson(context, "shandong.json"));
			System.out.println(getJson(context, "shanghai.json"));
			System.out.println(jsonArr.toString());
			
			
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	

	
	//取本地json文件
	public static String getJson(Context context, String fileName) {  
		  
        StringBuilder stringBuilder = new StringBuilder();  
        try {  
            AssetManager assetManager = context.getAssets();  
            BufferedReader bf = new BufferedReader(new InputStreamReader(  
                    assetManager.open(fileName)));  
            String line;  
            while ((line = bf.readLine()) != null) {  
                stringBuilder.append(line);  
                System.out.println(line);
            }  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return stringBuilder.toString();  
    }  
//	//缓存图片
//	public static void cacheFrontPageBitmap(Context context,Bitmap bitmap,String gid){
//		mCache = ACache.get(context);
//		if(mCache.getAsBitmap(KEY_GAME_FRONT_PAGE+gid)!= null){
//			mCache.remove(KEY_GAME_FRONT_PAGE+gid);
//		}
//		mCache.put(KEY_GAME_FRONT_PAGE, gid, CACHE_TIME);
//	}
//	
//	public static Bitmap getCachedFrontPageBitmap(Context context, String gid){
//		mCache = ACache.get(context);
//		return mCache.getAsBitmap(KEY_GAME_ID+gid);
//	}
//	
//	//给定url，返回bitmap
//	public static Bitmap returnBitMap(String url,String path) {
////        URL myFileUrl = null;
//		new AsyncTask<Void, Void, String>() {
//
//			@Override
//			protected String doInBackground(Void... arg0) {
//				//配置上传的参数
//				try{
//				HttpURLConnection uc;
//				uc = (HttpURLConnection) new URL(
//						"http://101.200.214.68/Uploads/Picture/GamePic/2015-11-04/5639c7f545377.jpg")
//						.openConnection();
//				uc.setDoOutput(true);
//				uc.setRequestMethod("POST");
//				uc.setUseCaches(false);
//				uc.setRequestProperty("Connection", "Keep-Alive");
//				BufferedReader br = new BufferedReader(new InputStreamReader(
//						uc.getInputStream(), "utf-8"));
//				String line = null;
//				StringBuffer result = new StringBuffer();
//				while ((line = br.readLine()) != null) {
//					result.append(line);
//				}
//				System.out.println(uc.getURL());
//				System.out.println("result:" + result);
//				br.close();
//				return result.toString();
//				} catch (MalformedURLException e) {
//					e.printStackTrace();
//					return null;
//				} catch (IOException e) {
//					e.printStackTrace();
//					return null;
//				}
//				
//			}
//			
//			@Override
//			protected void onPostExecute(String result) {
//				
//				super.onPostExecute(result);
//				bitmapString = result;
//				return;
//			}
//		}.execute();
//		return BitmapFactory.decodeByteArray(bitmapString.toString().getBytes(), 0, bitmapString.toString().getBytes().length);
//	}
//	
//    public static byte[] getImage(String path, String imagePath) throws Exception {  
//        URL url = new URL(path);  
//        HttpURLConnection httpURLconnection =  (HttpURLConnection)url.openConnection();  
////        httpURLconnection.setRequestMethod("POST");
//        httpURLconnection.setDoOutput(true);
////        httpURLconnection.setUseCaches(false);
////        httpURLconnection.setRequestProperty("Connection", "Keep-Alive");
////        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(httpURLconnection.getOutputStream(),Config.CHARSET));
////        bw.write(imagePath.toString());
////		bw.flush();
////		bw.close();
//        InputStream in = null;  
//        byte[] b = new byte[1024*3];  
//        int len = -1;  
//        if (httpURLconnection.getResponseCode() == 200) {  
//             in = httpURLconnection.getInputStream();  
//             byte[] result = readStream(in);  
//             in.close();  
//             return result;  
//               
//        }  
//        return null;  
//    }  
//      
//    public static byte[] readStream(InputStream in) throws Exception{  
//        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();  
//        byte[] buffer = new byte[1024*3];  
//        int len = -1;  
//        while((len = in.read(buffer)) != -1) {  
//            outputStream.write(buffer, 0, len);  
//        }  
//        outputStream.close();  
//        in.close();  
//        return outputStream.toByteArray();  
//    }  
	//获取系统任务栏高度
    public static int getDimensionMiss(Context context) {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        return resources.getDimensionPixelSize(resourceId);
    }
}
