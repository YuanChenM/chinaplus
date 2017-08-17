/**
 * trf-cnst.js
 * 
 * @screen COMMON		
 * @author COMMON
 */
/********** 定数 **********/
Ext.namespace('TRF.cnst');

TRF.cnst.APPLICATION_TABPANNEL_ID = 'infotab'; //info tab
TRF.cnst.APPLICATION_BANNER_ID = 'trfbanner'; //info tab
TRF.cnst.VIEWPORT_ID = 'viewportId';

/** 設定値(各コンポーネント) */
TRF.cnst.ROOT = 'swapData';
TRF.cnst.TOTALPROPERTY = 'totalCount';
TRF.cnst.RESULTCODE ='resultCode';
TRF.cnst.MESSAGECODE ='messageCode';
TRF.cnst.MESSAGEARGUMENTS ='messageArguments';
TRF.cnst.FIELDERROR ='fieldError';
TRF.cnst.FILTER_NAME = 'trfFilterMap';
TRF.cnst.FILTERS = 'filters';
TRF.cnst.SELECTEDDATAS = 'selectedDatas';
TRF.cnst.DATAS = 'datas';
TRF.cnst.JSONDATA = 'jsonData';
TRF.cnst.SCREENID = 'screenId';
TRF.cnst.TOKEN = 'token';
TRF.cnst.CLIENTTIME = 'clientTime';
TRF.cnst.MAINSCREEN = 'CPCMSS01';
TRF.cnst.AUTH_OFFICE = 'authOfficeId';

/** 設定値(WEBアプリケーション) */
TRF.cnst.BLANK_IMAGE_URL = 'images/s.gif';	// ブランクイメージ
TRF.cnst.DEBUG_MODE = '0';										// debug mode（0:nomal，1:debug）
TRF.cnst.PERFORMANCE_LOG_MODE = '0';										// debug mode（0:nomal, 1:debug）
TRF.cnst.JSERROR_LOG_MODE = '0';  

/** action */
TRF.cnst.ACTION_CD_INIT = 'init';  		// init
TRF.cnst.ACTION_CD_SEARCH = 'search';		// search
TRF.cnst.ACTION_CD_SAVE = 'save';			// save
TRF.cnst.ACTION_CD_CANCEL = 'cancel';		// cancel
TRF.cnst.ACTION_CD_DOWNLOAD = 'download';	// download
TRF.cnst.ACTION_CD_UPLOAD = 'upload';		// upload
TRF.cnst.ACTION_CD_LOAD = 'load';			// load
TRF.cnst.ACTION_CD_ADD = 'add';				// add
TRF.cnst.ACTION_CD_SWITCHPAGE = 'switchPage';	// switchPage
TRF.cnst.ACTION_CD_REFRESH = 'refresh';		// refresh
TRF.cnst.ACTION_CD_REMOVE = 'remove';
TRF.cnst.ACTION_CD_MODIFY = 'modify';
TRF.cnst.ACTION_CD_COMPLETE = 'complete';

/** メッセージコード */
TRF.cnst.MESSAGE_CODE_e0004 = 'e0004';

/**set timeout*/
TRF.cnst.sessionTimeout=1800000;
TRF.cnst.simpleSubmitTimeout=1800000;
TRF.cnst.actionSubmitTimeout=1800000;
TRF.cnst.uploadSubmitTimeout=1800000;
TRF.cnst.downloadSubmitTimeout=1800000;
TRF.cnst.getApplicationContentsTimeout=1800000;
TRF.cnst.storeLoadTimeout=1800000;

/**The size of pop up screen*/
TRF.cnst.POPUP_XLARGE='950*600';
TRF.cnst.POPUP_LARGE='800*500';
TRF.cnst.POPUP_MIDIUM='600*350';
TRF.cnst.POPUP_SMALL='400*200';

/** Grid size */
TRF.cnst.PAGE_SIZE_COMMON = 20;
TRF.cnst.PAGE_SIZE_ALL=500;

/** result code */
TRF.cnst.RESULT_CODE_SESSION_TIMEOUT = '401';// session timeout
TRF.cnst.RESULT_CODE_EXCEPTION_ERROR = '201';// exception timeout
TRF.cnst.NETWORK_ERROR = '0';// network error
TRF.cnst.RESULT_SUCCESS = '200';// success

// global system time zone number code, default value +9
TRF.cnst.SYSTEM_TIME_ZONE_NUMBER = +9;
TRF.cnst.CONSECUTIVE_CHARACTERS = 3;
TRF.cnst.MAX_DIGIT = 3;
TRF.cnst.GLOBAL_DATE_FORMAT = 'Y/m/d';
TRF.cnst.GLOBAL_DATETIME_FORMAT = 'Y/m/d H:i';
TRF.cnst.SHORT_TIME_FORMAT = 'H:i';
TRF.cnst.SHORT_TIME_WITH_SECOND_FORMAT = 'H:i:s';
TRF.cnst.GLOBAL_TIMESTAMP_FORMAT = 'Y-m-d H:i:s.u';
TRF.cnst.GLOBAL_DATETIME_LONG_FORMAT = 'd M Y H:i';
TRF.cnst.GLOBAL_DATETIME_LONG_WITH_SECOND_FORMAT = 'd M Y H:i:s';
TRF.cnst.GLOBAL_DATETIME_WITH_MS_FORMAT = 'YmdHisu';
TRF.cnst.GLOBAL_DATETIME_SHORT_FORMAT = 'd M Y';
TRF.cnst.GLOBAL_YEAR_MONTH_FORMAT = 'M Y';
TRF.cnst.GLOBAL_MONTH_FORMAT = 'M';
TRF.cnst.GLOBAL_YEAR_AND_MONTH_FORMAT = 'Ym';
TRF.cnst.GLOBAL_YEAR_AND_MONTH_AND_DAY_FORMAT = 'Ymd';
TRF.cnst.GLOBAL_TIME_FORMAT_12 = 'g:i A';
TRF.cnst.GLOBAL_MONTH_DAY_FORMAT = 'd-M';
TRF.cnst.GLOBAL_DATETIME_LONG_WITH_SECOND_FORMAT_12 = 'd M Y H:i A';
TRF.cnst.GLOBAL_DATE_YEAR_MONTH_DAY_FORMAT = 'Y-m-d';
TRF.cnst.GLOBAL_DATE_YEAR_MONTH_FORMAT = 'Y-m';

/**
 * Message box type.
 */
TRF.cnst.MESSAGEBOX_TYPE_INFO = 'INFO';
TRF.cnst.MESSAGEBOX_TYPE_WARN = 'WARN';
TRF.cnst.MESSAGEBOX_TYPE_ERROR = 'ERROR';
TRF.cnst.MESSAGEBOX_TYPE_QUESTION = 'QUESTION';

TRF.cnst.INDEX_PAGE="login";
TRF.cnst.MAIN_PAGE="main";
TRF.cnst.INFORMATION_PAGE="information.html";
TRF.cnst.COPY_RIGHT_INFO='Copyright &copy; 2014 TOYOTA TSUSHO CORPORATION. All Rights Reserved.';