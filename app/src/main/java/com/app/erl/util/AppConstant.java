package com.app.erl.util;

/**
 * Created by Dhaval on 05-02-2018.
 */

public final class AppConstant {
    public static final String arrColorList[] = {"#b7bfc6", "#e8384f", "#fd612c", "#fd9a00", "#eec300", "#a4cf30"
            , "#62d26f", "#37c5ab", "#20aaea", "#0070e0", "#7a6ff0", "#aa62e3", "#e362e3", "#ea4e9d", "#fc91ad", "#8da3a6"};

    public static final String DEVICE_TYPE = "1";
    public static final int DATA_PER_PAGE = 20;
    public static final int MAX_ALBUM_LENGTH = 8;
    public static final int UNAUTHORIZED = 401;
    public static final String ERROR_UNKNOWN = "ERR0001";
    public static final String EXTRA_CHANNEL_SID = "C_SID";

    public static final float ALPHA_DIM_VALUE = 0.1f;
    public static final float ALPHA_NORMAL_VALUE = 1f;

    public static final int PWD_MIN_LENGTH = 6;
    public static final int PWD_MAX_LENGTH = 12;

    public static final int PHONE_NUMBER_MIN_LENGTH = 8;
    public static final int PHONE_NUMBER_MAX_LENGTH = 15;

    public static boolean IS_WANT_TOAST_ICON = true;
    public static boolean IS_NOT_WANT_TOAST_ICON = false;
    public static final int STATUS_CODE_200 = 200;

    public static final int MAX_IMAGE_WIDTH = 1280;
    public static final int MAX_IMAGE_HEIGHT = 1280;

    public static final int IMAGE_QUALITY = 80;


    public static final class DialogIdentifier {
        public static final int LOGOUT = 1;
        public static final int DIALOG_SELECT_COMPANY_SIZE = 2;
        public static final int DIALOG_SELECT_TIME_ZONE = 3;
        public static final int VERIFY_COMPANY_CODE = 4;
        public static final int VERIFY_EMAIL = 5;
        public static final int SELECT_USER = 6;
        public static final int SELECT_TASK_TYPE = 7;
        public static final int SELECT_PRIORITY = 8;
        public static final int SELECT_COMPLETED_PERCENTAGE = 9;
        public static final int SELECT_STATUS = 10;
        public static final int SELECT_TASK = 11;
        public static final int CLOCK_OUT = 12;
        public static final int COUNTRY_LIST = 13;
        public static final int EMERGENCY_CONTACT_EXTENSION = 14;
        public static final int HOME_CONTACT_EXTENSION = 15;
        public static final int MOBILE_NUMBER_EXTENSION = 16;
        public static final int PROJECT_ITEM_MENU = 17;
        public static final int DELETE_PROJECT = 18;
        public static final int TASK_ITEM_MENU = 19;
        public static final int DELETE_TASK = 20;
        public static final int PROJECT_SIGN_OUT = 21;
        public static final int LOCATION_LIST = 22;
        public static final int SELECT_USER_TYPE = 23;
        public static final int SELECT_TRADE = 24;
        public static final int VERIFIED_PROFILE = 25;
        public static final int REJECT_USER_REQUEST = 26;
        public static final int REMOVE_USER = 27;
        public static final int TASK_NOT_FOUND = 28;
        public static final int CURRENCY = 29;
        public static final int UPDATE_APP = 30;
        public static final int CLEAR_FILTER = 31;
        public static final int CANCEL_ORDER = 32;
        public static final int REJECT_ORDER = 33;
        public static final int HEIGHT_LIST = 34;
        public static final int WEIGHT_LIST = 35;
        public static final int SELECT_SORT_BY = 36;
        public static final int SELECT_TAG_USER = 37;
        public static final int DISPLAY_LOGGED_IN_USER = 38;
        public static final int COMPLETE_PROFILE_DIALOG = 39;
        public static final int PROJECT_FILTERS = 40;
        public static final int MARK_AS_COMPLETED_PROJECT = 41;
        public static final int REOPEN_PROJECT = 42;
        public static final int SELECT_LANGUAGE = 43;
        public static final int SELECT_LEAVE_TITLE = 44;
        public static final int SELECT_LEAVE_TYPE = 45;
        public static final int REJECT_LEAVE = 46;
        public static final int CANCEL_LEAVE = 47;
        public static final int EDIT_SUPERVISOR = 48;
        public static final int DELETE_USERS = 49;
        public static final int ADD_FOLDER = 50;
        public static final int ADD_TASK = 51;
        public static final int ADD_PROJECT = 52;
        public static final int ADD_PERSONAL_PROJECT = 53;
        public static final int DELETE_TEAM = 54;
        public static final int APPROVE_ALL_RECORD = 55;
        public static final int SELECT_REASON = 56;
        public static final int DELETE_ITEM = 57;
        public static final int DOCUMENTS_UPLOAD = 58;
        public static final int DONT_REMEMBER_PASSWORD = 59;
        public static final String SELECT_FROM_DATE = "from_date";
        public static final String SELECT_TO_DATE = "to_date";
    }

    public static final class IntentKey {
        public static final String USER_ID = "user_id";
        public static final String PROJECT_ID = "project_id";
        public static final String PROJECT_USER_ID = "project_user_id";
        public static final String PROJECT_TITLE = "project_title";
        public static final String PROJECT_DATA = "project_data";
        public static final String TASK_DATA = "task_data";
        public static final String TASK_ID = "task_id";
        public static final String TASK_TITLE = "task_title";
        public static final String POSITION = "POSITION";
        public static final String TYPE = "type";
        public static final String IMAGE_URI = "image_uri";
        public static final String CROP_RATIO_X = "crop_ratio_X";
        public static final String CROP_RATIO_Y = "crop_ratio_Y";
        public static final String FILE_EXTENSION = "file_extension";
        public static final String TASK_CHECK_LOG_DATA = "task_check_log_data";
        public static final String IS_FROM_NOTIFICATION = "is_from_notification";
        public static final String NOTIFICATION_TYPE = "notification_type";
        public static final String IS_PROJECT_SIGN_OUT = "is_project_sign_out";
        public static final String FILTER_DATE = "filter_date";
        public static final String ADDRESS = "address";
        public static final String LATITUDE = "latitude";
        public static final String LONGITUDE = "longitude";
        public static final String FILTER_TYPE = "filter_type";
        public static final String FILTER_IDS = "filter_ids";
        public static final String ORDER_ID = "order_id";
        public static final String FROM_ORDER = "from_order";
        public static final String SELECTED_IMAGES = "selected_images";
        public static final String WEB_URL = "web_url";
        public static final String NEAR_MISS_INCIDENT_ID = "near_miss_incident_id";
        public static final String COMPANY_DETAILS = "company_details";
        public static final String DASHBOARD_DATA = "dashboard_data";
        public static final String SIGN_IN_WORK_MANAGER_TAG = "sign_in_work_manager_tag";
        public static final String SIGN_IN_WORK_MANAGER_UNIQUE = "sign_in_work_manager_unique";
        public static final String VIEW_MAP_TYPE = "view_map_type";
        public static final String MODULE_INFO = "module_info";
        public static final String SIGN_UP_REQUEST_DATA = "sign_up_request";
        public static final String FOLDER = "folder";
        public static final String FOLDER_TYPE = "folder_type";
        public static final String FILE_TYPE = "file_type";
        public static final String ACTION = "action";
        public static final String TEAM_INFO = "team_info";
        public static final String TEAM_ID = "team_id";
        public static final String TEAM_TITLE = "team_title";
        public static final String TEAM_MEMBERS = "team_members";
        public static final String CONTACT_INFO = "contact_info";
        public static final String ID = "id";
        public static final String FOLDER_ID = "folder_id";
        public static final String TIMESHEET_LOG_INFO = "timesheet_log_info";
        public static final String FILTER_DATA = "FILTER_DATA";
        public static final String ATTACHMENT_IDS = "ATTACHMENT_IDS";
        public static final String ATTACHMENTS = "ATTACHMENTS";
        public static final String TITLE = "TITLE";

        public static final int ADD_PROJECT = 1;
        public static final int TASK_LIST = 2;
        public static final int ADD_TASK = 3;
        public static final int TASK_DETAILS = 4;
        public static final int CLOCK_OUT = 5;
        public static final int EXTERNAL_STORAGE_PERMISSION = 6;
        public static final int REQUEST_GALLERY = 7;
        public static final int REQUEST_CAMERA = 8;
        public static final int REQUEST_CAMERA_KITKAT = 9;
        public static final int REQUEST_CROP_IMAGE = 10;
        public static final int UPDATE_PROJECT = 11;
        public static final int PROJECT_SIGN_OUT = 12;
        public static final int MY_LOCATION = 13;
        public static final int VIEW_USER = 14;
        public static final int FILTER_REQUEST = 15;
        public static final int GOODS_REQUEST_PRODUCTS = 16;
        public static final int VIEW_CART = 17;
        public static final int VIEW_ORDER_DETAILS = 18;
        public static final int SELECT_GALLERY_PHOTOS = 19;
        public static final int CREATE_REPORT_INCIDENT = 20;
        public static final int AUDIO_PERMISSION = 21;
        public static final int EDIT_PROFILE = 22;
        public static final int ADD_LEAVE = 23;
        public static final int ADD_USERS = 24;
        public static final int ARCHIVE_USERS = 25;
        public static final int LOCATION_SETTING_STATUS = 26;
        public static final int VIEW_PROJECTS = 27;
        public static final int VIEW_TEAM_USER = 28;
        public static final int ADD_TEAM = 29;
        public static final int ADD_TEAM_MEMBER = 30;
        public static final int ADD_CONTACT = 31;
        public static final int EDIT_CONTACT = 32;
        public static final int ADD_FOLDER = 33;
        public static final int SELECT_PDF = 34;
        public static final int VIEW_TIMESHEET_LOG = 35;
        public static final int FILTER_USERS = 36;
        public static final int ARCHIVE_PROJECT = 37;
        public static final int CREATE_REPORT_ISSUE = 38;
        public static final int SELECT_DOCUMENT_FROM_PROJECT = 39;
    }

    public static final class AppLanguage {
        public static final String ISO_CODE_ENG = "en";
    }


    public static final class SharedPrefKey {
        public static final String USER_INFO = "USER_INFO";
        public static final String APP_LANGUAGE = "APP_LANGUAGE";
        public static final String REAL_TIME_DB_ID = "REAL_TIME_DB_ID";
        public static final String PERMISSION_SETTINGS = "PERMISSION_SETTINGS";
        public static final String NOTIFICATION_SETTINGS = "NOTIFICATION_SETTINGS";
        public static final String THEME_MODE = "THEME_MODE";
    }

    public static final class Type {
        public static final int PENDING_REQUEST = 1;
        public static final int REJECTED_REQUEST = 2;
        public static final int IMAGE = 1;
        public static final int AUDIO = 2;
        public static final String VERIFY_EMAIL = "verify_email";
        public static final String VERIFY_FORGOT_PASSWORD_EMAIL = "verify_forgot_password_email";
        public static final String VERIFY_TOKEN = "verify_token";
        public static final String PROFILE_VERIFIED = "profile_verified";
        public static final String DESCRIPTION = "description";
        public static final String TITLE = "title";
        public static final String START_DATE = "start_date";
        public static final String END_DATE = "end_date";
        public static final String START_TIME = "start_time";
        public static final String END_TIME = "end_time";
        public static final String TASK_COMPLETED = "task_completed";
        public static final String PRIORITY = "priority";
        public static final String LOCATION = "location";
        public static final String STATUS = "status";
        public static final String USERS = "users";
        public static final String CAMERA = "camera";
        public static final String DIGITAL_CARD = "digital_card";
        public static final String ADD = "add";
        public static final String CHECK = "1";
        public static final String UNCHECK = "0";
        public static final int SINGLE_USER = 1;
        public static final int MULTIPLE_USER = 2;
        public static final int LEAVE_PENDING = 1;
        public static final int LEAVE_ACCEPTED = 2;
        public static final int LEAVE_REJECTED = 3;
        public static final int PERSONAL_FILE = 1;
        public static final int PROJECTS_FILE = 2;
        public static final int ARCHIVE_FILE = 3;

        public static final int CHECK_IN = 1;
        public static final int CHECK_OUT = 2;
        public static final int BREAK_IN = 3;
        public static final int BREAK_OUT = 4;
        public static final int CLOCK_IN = 5;
        public static final int CLOCK_OUT = 6;

        public static final int PROJECT = 1;
        public static final int FOLDER = 2;
        public static final int SUB_FOLDER = 3;
        public static final int TASK = 4;

        public static final int FILTER_COMPANY_USERS = 1;
        public static final int FILTER_TEAM_USERS = 2;
    }


    public static final class Action {
        public static final int EMPLOYEE_LIST = 1;
        public static final int ARCHIVE_LIST = 2;
        public static final int ADD_GROUP_MEMBER = 3;
        public static final int PROJECT_USERS = 4;
        public static final int FOLDER_USERS = 5;
        public static final int TASK_USERS = 6;
        public static final int COMPANY_USERS = 7;
        public static final int EDIT_INFO = 8;
        public static final int DOCUMENTS = 9;
        public static final int ADD_TASK = 10;
        public static final int ADD_FOLDER = 11;
        public static final int EXTRA_WORK = 12;
        public static final int HIDE = 13;
        public static final int ARCHIVE = 14;
        public static final int ADD_INTO_PROJECT = 15;
        public static final int ADD_INTO_FOLDER = 16;
        public static final int Edit_FOLDER = 17;
        public static final int Edit_TASK = 18;
        public static final int REMOVE_CONTACT = 19;
        public static final int EDIT_CONTACT = 20;
        public static final int TEAM_MEMBERS = 21;
        public static final int SHOW_FEED_MENU_DIALOG = 22;
        public static final int MOVE_TO_HOME_TAB = 23;
        public static final int VIEW_TASK = 24;
        public static final int START_TASK = 25;
        public static final int STOP_TASK = 26;
        public static final int REPORT_ISSUE = 27;
        public static final int SELECT_PERCENTAGE = 28;
        public static final int DELETE = 29;
    }

    public static final class UserType {
        public static final int ADMIN = 1;
        public static final int SUPERVISOR = 2;
        public static final int PROJECT_MANAGER = 3;
        public static final int EMPLOYEE = 4;
        public static final int DRIVER = 5;
    }

    public static final class AvailabilityStatus {
        public static final int AVAILABLE = 1;
        public static final int BUSY = 2;
        public static final int OFF_WORK = 3;
    }

    public static final class THEME_MODE {
        public static final int LIGHT = 0;
        public static final int DARK = 1;
    }

    public static final class FilterType {
        public static final int PROJECT = 1;
        public static final int TASK = 2;
        public static final int ASSIGNEE = 3;
    }

    public static final class SortBy {
        public static final int DAY = 1;
        public static final int WEEK = 2;
        public static final int MONTH = 3;
    }

    public static final class LocationMode {
        public static final int LOCATION_MODE_OFF = 0;
        public static final int LOCATION_MODE_SENSORS_ONLY = 1;
        public static final int LOCATION_MODE_BATTERY_SAVING = 2;
        public static final int LOCATION_MODE_HIGH_ACCURACY = 3;
    }

    public static final class FileExtension {
        public static final String JPG = ".jpg";
        public static final String PNG = ".png";
        public static final String PDF = ".pdf";
        public static final String MP3 = ".mp3";
        public static final String M4A = ".m4a";
    }

    public static final class Directory {
        public static final String DEFAULT = "owlmanagement";
        public static final String PDF = "owlmanagement/digitalcard";
        public static final String IMAGES = "owlmanagement/images";
        public static final String AUDIO = "owlmanagement/audio";
    }

    public static final class ShowCasePrefKey {
        public static final String IS_DASHBOARD_COMPLETED = "IS_DASHBOARD_COMPLETED";
    }


    public static final String APP_APK_NAME = "RetailerNavneetDev.apk";

}
