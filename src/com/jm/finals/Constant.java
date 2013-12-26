package com.jm.finals;

import android.graphics.Color;

/**
 * 用来定义常量
 * 
 * @author win
 * 
 */
public class Constant {

	// 版本信息
	public static final String VERSION = "1";

	public final static String DEFAULT_SERVER = "http://wap.faxingw.cn/index.php";
	public final static String CRASH_LOG_UPDATE = "";

	public static final String CONSUMER_KEY = "276585644";// 替换为开发者的appkey，例如"1646212860";
	public static final String REDIRECT_URL = "http://www.faxingw.cn";
	public final static String APP_ID = "100478968";// 腾讯
	public final static String URN_HAIRLIST_LIST = "?m=soufaxing&a=index";
	public final static String URN_TYPE_LIST = "?m=soufaxing&a=type";
	public final static String URN_MYCOLLECTLIST_LIST = "http://wap.faxingw.cn/index.php?m=User&a=collectlist";
	public final static String URN_MYWORKLIST_LIST = "http://wap.faxingw.cn/index.php?m=User&a=workslist";
	public final static String URN_MYWORKDOLIST_LIST = "http://wap.faxingw.cn/index.php?m=Willdo&a=willDoList";
	
	
	public final static String URN_ALLDONGTAI_LIST = "http://wap.faxingw.cn/index.php?m=Dynamic&a=allnews";
	public final static String URN_ZHAOFAXING_LIST = "http://wap.faxingw.cn/index.php?m=Forhair&a=newCate";

	public final static String URN_SETTING = "http://wap.faxingw.cn/index.php?m=User&a=site";
	public final static String URN_TIPSSETTINGCHANGE = "http://wap.faxingw.cn/index.php?m=User&a=setting";
	public final static String URN_TIPSSETTING = "http://wap.faxingw.cn/index.php?m=User&a=settinginfo";
	public final static String URN_GETCHANGINFO = "http://wap.faxingw.cn/index.php?m=User&a=data_info";
	public final static String URN_HAIRINFO_LIST = "http://wap.faxingw.cn/index.php?m=Dynamic&a=workinfo";

	public final static String URN_HUATIINFO = "http://wap.faxingw.cn/index.php?m=Infostation&a=skillview";

	public final static String URN_NEWS_INFO = "http://wap.faxingw.cn/index.php?m=Infostation&a=newsview";
	public final static String URN_YUYUE_HAIER_LIST = "http://wap.faxingw.cn/index.php?m=Reserve&a=reserve_run";
	public final static String URN_YUYUE_HISTORY = "http://wap.faxingw.cn/index.php?m=Reserve&a=history";
	public final static String URN_YUYUE_CURRENT = "http://wap.faxingw.cn/index.php?m=Reserve&a=current";
	public final static String URN_YUYUE_INFO = "http://wap.faxingw.cn/index.php?m=Reserve&a=reserve_info";
	public final static String URN_YUYUE_COMMENT_INFO = "http://wap.faxingw.cn/index.php?m=Reserve&a=read_reviews";

	public final static String URN_NEAR_STOREHAIRLIST = "http://wap.faxingw.cn/index.php?m=Near&a=hairstylist";
	public final static String URN_NEAR_STORE = "http://wap.faxingw.cn/index.php?m=Near&a=store";
	public final static String URN_CHANGE_STORE = "http://wap.faxingw.cn/index.php?m=User&a=legalize";
	public final static String URN_COMMENT_LIST = "http://wap.faxingw.cn/index.php?m=Works&a=commentlist";
	public final static String URN_PRICE_LIST = "http://wap.faxingw.cn/index.php?m=Reserve&a=check_prices";

	public final static String URN_SENDLOCATION = "http://wap.faxingw.cn/index.php?m=User&a=coordinates";
	public final static String URN_MSG_LIST = "http://wap.faxingw.cn/index.php?m=Message&a=index";

	public final static String URN_MYQUESTION = "http://wap.faxingw.cn/index.php?m=Problem&a=myquestion";
	public final static String URN_MYANSWER = "http://wap.faxingw.cn/index.php?m=Problem&a=answerlist";
	public final static String URN_FINDQUESTION = "http://wap.faxingw.cn/index.php?m=Problem&a=findissue";

	public final static String URN_QUESION_LIST = "http://wap.faxingw.cn/index.php?m=Problem&a=topicview";
	public final static String URN_CHAT_LIST = "http://wap.faxingw.cn/index.php?m=Message&a=talk";
	public final static String URN_CHAT_PUBLISH = "http://wap.faxingw.cn/index.php?m=Message&a=publish";
	public final static String URN_CHAT_QUESTION = "http://wap.faxingw.cn/index.php?m=Problem&a=answeradd";

	public final static String URN_LIKE = "http://wap.faxingw.cn/index.php?m=Works&a=collection";

	public final static String URN_MYINFO = "http://wap.faxingw.cn/index.php?m=User&a=info";
	public final static String URN_YUYUE = "http://wap.faxingw.cn/index.php?m=Reserve&a=priceinfo";
	public final static String URN_GETUSERPHONE = "http://wap.faxingw.cn/index.php?m=User&a=phone";
	public final static String URN_YUYUECHECK = "http://wap.faxingw.cn/index.php?m=Reserve&a=reservation";

	public final static String URN_YUYUCHANGE = "http://wap.faxingw.cn/index.php?m=Reserve&a=edit_status";
	public final static String URN_TIPS_INFO = "http://wap.faxingw.cn/index.php?m=Reserve&a=notice_show";

	public final static String URN_SETTIPS_INFO = "http://wap.faxingw.cn/index.php?m=Reserve&a=notice";

	public final static String URN_MSG_PUSH = "http://wap.faxingw.cn/index.php?m=Message&a=push_andrews";
	public final static String URN_PRICE_INFO = "http://wap.faxingw.cn/index.php?m=Reserve&a=priceinfo";

	public final static String URN_SETPRICE_INFO = "http://wap.faxingw.cn/index.php?m=Reserve&a=priceset";

	public final static String URN_STORE_INFO = "http://wap.faxingw.cn/index.php?m=User&a=store_info";
	public final static String URN_JOBSVIEW = "http://wap.faxingw.cn/index.php?m=Infostation&a=jobsview";
	public final static String URN_ZHUANRANGVIEW = "http://wap.faxingw.cn/index.php?m=Infostation&a=transferview";

	public final static String URN_GUANZHU = "http://wap.faxingw.cn/index.php?m=User&a=follow";

	public final static String URN_SHALONGINFO = "http://wap.faxingw.cn/index.php?m=Works&a=store_info";
	public final static String URN_NEAR = "http://wap.faxingw.cn/index.php?m=Near&a=index";
	public final static String URN_JOB_LIST = "http://wap.faxingw.cn/index.php?m=Infostation&a=jobslist";
	public final static String URN_SKILLLIST = "http://wap.faxingw.cn/index.php?m=Infostation&a=skilllist";
	public final static String URN_SHALONGZHUANRANGIST = "http://wap.faxingw.cn/index.php?m=Infostation&a=transferlist";

	public final static String URN_USER_LIST = "http://wap.faxingw.cn/index.php?m=User&a=";

	public final static String URN_ALL_NEWSLIST = "http://wap.faxingw.cn/index.php?m=Infostation&a=newslist";
	public final static String URN_ALL_FAXINGSHI = "http://wap.faxingw.cn/index.php?m=Hairstylist&a=allstylists";
	public final static String URN_TONGCHENG_FAXINGSHI = "http://wap.faxingw.cn/index.php?m=Hairstylist&a=citystylists";
	public final static String URN_TUIJIAN_FAXINGSHI = "http://wap.faxingw.cn/index.php?m=Hairstylist&a=recomstylists";
	public final static String URN_GUANZHU_FAXINGSHI = "http://wap.faxingw.cn/index.php?m=Hairstylist&a=followstylists";

	public final static String URN_DONGTAI = "http://wap.faxingw.cn/index.php?m=Dynamic&a=ranking";

	public final static String URN_GETID = "http://wap.faxingw.cn/index.php?m=index&a=login";

	public final static String URN_SETID = "http://wap.faxingw.cn/index.php?m=User&a=binding";

	public final static String URN_GETVCODE = "http://wap.faxingw.cn/index.php?m=User&a=verify_code";

	public final static String URN_CHANGEINFO = "http://wap.faxingw.cn/index.php?m=User&a=data_modify";

	public final static String URN_PUBLICPORTFOLIO = "http://wap.faxingw.cn/index.php?m=Works&a=add_works";
	public final static String URN_PUBLICPORTFOLIOOFHAIR = "http://wap.faxingw.cn/index.php?m=Works&a=newAddWorks";
	
	
	public final static String URN_ADD_NEWS = "http://wap.faxingw.cn/index.php?m=Infostation&a=newsadd";

	public final static String URN_PUBLICQUESTION = "http://wap.faxingw.cn/index.php?m=Problem&a=quizadd";
	public final static String URN_ADDJOB = "http://wap.faxingw.cn/index.php?m=Infostation&a=jobsadd";
	public final static String URN_PUBLICSKILL = "http://wap.faxingw.cn/index.php?m=Infostation&a=skilladd";
	public final static String URN_ADDZHUANRANG = "http://wap.faxingw.cn/index.php?m=Infostation&a=transferadd";

	public final static String URN_ADDWORKLIST = "http://wap.faxingw.cn/index.php?m=User&a=workslist";
	public final static String URN_SUCCESS = "http://wap.faxingw.cn/index.php?m=User&a=success";
	public final static String URN_GETVOCE = "http://wap.faxingw.cn/index.php?m=User&a=verify_code";

	public final static String URN_COMMENT_YUYUE = "http://wap.faxingw.cn/index.php?m=Reserve&a=evaluate";
	public final static String URN_VCOCESUCCESS = "http://wap.faxingw.cn/index.php?m=User&a=success";
	public final static String URN_ADD_IMG = "?m=Up&a=add_img";

	public final static String URN_ADD_LOG = "http://wap.faxingw.cn/index.php?m=Up&a=add_log";

	public final static String URN_COMMENT = "http://wap.faxingw.cn/index.php?m=Works&a=comment";
	public final static String URN_ADDPOINT = "http://wap.faxingw.cn/index.php?m=User&a=score";
	public final static String URN_HUATICOMMENT = "http://wap.faxingw.cn/index.php?m=Infostation&a=commentadd";

	public final static String URN_OPINION = "http://wap.faxingw.cn/index.php?m=User&a=opinion";
	// //////////////////////////////////////////////////////////////
	public static final String PREFS_NAME = "faxingwu";
	public static final String SIGN_NUMBER = "sign";
	public static final String SIGN_POINT = "signpoint";
	public static final String SIGN_TIME = "time";
	public static final String SIGN_GOODSGROUP_SAVETIME = "goodsgroupsavetime";
	public static final String SIGN_GOODSEXCHANGE_SAVETIME = "goodsexchangetime";
	public static final String CRASH_FILE_NAME = "crash-fxw.log";
	public static final String ZIP = ".zip";
	public static final String PREFS_USERNAME_KEY = "username";
	public static final String PREFS_ZIPVERSION_KEY = "zipversion";

	public final static String REQID_FILE_UPLOAD = "101";
	public static final int MINUTE = 60 * 1000;
	public static final String LOG_TAG = "WANGZHE";
	public final static String CODE_SUCCESSFUL = "101";
	public static final int DOWNLOAD_SUCCESS = 1;
	public static final int DOWNLOAD_FAILTURE = 2;
	public static final String CONTENTLENGTH = "contentLength";
	public static final String TEMP = ".tmp";
	public static final String APP_END = ".apk";

	public static final String ENCODING = "UTF-8";
	public static final int color_RoseRed = Color.rgb(240, 28, 97);
	public static final int color_Black = Color.rgb(0, 0, 22);
	public static final int color_Gary = Color.rgb(111, 111, 111);

}
