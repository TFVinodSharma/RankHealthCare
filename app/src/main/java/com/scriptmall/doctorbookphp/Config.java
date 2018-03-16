package com.scriptmall.doctorbookphp;

/**
 * Created by SCRIPTSMALL on 6/2/2017.
 */

public class Config {

    public static final String domain_name ="http://fxwebsolution.com/demo/doctorsearch/";
    public static final String folder =domain_name+"restapi/";

    public static final String IMG_URL =domain_name+"uploads/profile/";

    public static final String LOGO_URL = folder+"mainurl.php";
    public static final String TERMS_URL = folder+"terms.php";

    public static final String LOGIN_URL = folder+"login.php";
    public static final String FORGOTPWD_URL = folder+"forgotpassword.php";
    public static final String USERREG_URL = folder+"register.php";
    public static final String COUNTRY_URL = folder+"countrylist.php";
    public static final String STATE_URL = folder+"statelist.php";
    public static final String CITY_URL = folder+"citylist.php";

    public static final String MY_REVIEWS_URL = folder+"myreviews.php";
    public static final String REVIEW_DELETE_URL = folder+"delete_review.php";
    public static final String MY_APPOIRNTMENT_URL = folder+"myappointments.php";
    public static final String APPOINTMENT_CANCEL_URL = folder+"cancel_appointment.php";

    public static final String USER_REVIEWS_URL = folder+"user_reviews.php";
    public static final String USER_APPOINTMENT_URL = folder+"user_appointment.php";
    public static final String USER_APPOINTMENT_CANCEL_URL = folder+"app_status.php";

    public static final String PROFILE_DOCTOR_URL = folder+"view_doctorprofile.php";
    public static final String SPLLIST_URL = folder+"categorylist.php";
    public static final String MANAGE_LICENCE_URL = folder+"edit_doctorprofile.php";

    public static final String SEARCH_CITY_URL = folder+"citywise_search.php";
    public static final String SEARCH_URL = folder+"mainsearch.php";

    public static final String BOOK_URL = folder+"bookdoctor.php";
    public static final String DETAILS_URL = folder+"details.php";

    public static final String REVIEW_SEND_URL = folder+"postreview.php";
    public static final String REVIEW_DETAILS_URL = folder+"reviewdetails.php";

    public static final String PASSWORDUPDATE_URL = folder+"changepassword.php";
    public static final String EDITPROFILE_URL = folder+"editprofile.php";
    public static final String PROFILE_URL = folder+"viewprofile.php";
    public static final String IMAGEUPLOAD = folder+"imageupload.php";
    public static final String IMAGEREMOVE= folder+"delete_profile.php";

    public static final String BECOME_DOCTOR_URL= folder+"becomedoctor.php";

    public static final String KEY_EMAIL = "email";
    public static final String KEY_PWD = "pwd";

    public static final String CATID = "catid";
    public static final String CATNAME = "category_name";
    public static final String CATIMG = "catimg";

    public static final String SUBCATID = "subcatid";
    public static final String SUBCATNAME = "subcatname";
    public static final String SUBCATIMG = "subcatimg";

    public static final String UID = "userid";
    public static final String UTYPE = "user_role";
    public static final String UFNAME = "fname";
    public static final String ULNAME = "lname";
    public static final String UPHONENO = "phone";
    public static final String UMAIL = "email";
    public static final String UPWD = "pwd";
    public static final String UIMG = "img";
    public static final String UBUILD = "building";
    public static final String USTREET = "street";
    public static final String UAREA = "area";
    public static final String COUNTRY = "country";
    public static final String STATE = "state";
    public static final String CITY = "city";
    public static final String ZIP = "zip_code";
    public static final String OPWD = "opwd";
    public static final String CPWD = "cpwd";
    public static final String NPWD = "npwd";
    public static final String UIMGSTR = "image";
    public static final String UIMAGENAME = "imagename";

    public static final String DOCID = "docid";
    public static final String DOCNAME = "docname";
    public static final String RDATE = "postedon";
    public static final String RDESC = "comment";
    public static final String RID = "review_id";
    public static final String BID = "app_id";
    public static final String BDATE = "book_date";
    public static final String BTIME = "book_time";
    public static final String BSTATUS = "status";

    public static final String USERNAME = "username";


    public static final String BOOKDATE = "book_date";
    public static final String BOOKTIME = "time";
    public static final String TYPE = "casetype";
    public static final String CMT = "comments";

    public static final String BARID = "licenceid";
    public static final String ISSUEDATE = "licenceissue_date";
    public static final String EXPIN = "exp_in";
    public static final String SINCE = "practice_since";
    public static final String FEES = "counselling_fee";
    public static final String LANG = "language";
    public static final String OPHONE = "offc_phone";
    public static final String OADDR = "offc_addr";
    public static final String WEBSITE = "website";
    public static final String PIN = "practice_in";
    public static final String ABOUT = "about";
    public static final String UBUILD1 = "building1";
    public static final String USTREET1 = "street1";
    public static final String UAREA1 = "area1";

    public static final String MONFROM = "mon_from";
    public static final String MONTO = "mon_to";
    public static final String TUEFROM = "tue_from";
    public static final String TUETO = "tue_to";
    public static final String WEDFROM = "wed_from";
    public static final String WEDTO = "wed_to";
    public static final String THURSFROM = "thu_from";
    public static final String THURSTO = "thu_to";
    public static final String FRIFROM = "fri_from";
    public static final String FRITO = "fri_to";
    public static final String SATFROM = "sat_from";
    public static final String SATTO = "sat_to";
    public static final String SUNFROM = "sun_from";
    public static final String SUNTO = "sun_to";

    public static final String DOCADDR = "docaddress";
    public static final String EXP = "experience";
    public static final String RATING = "rating";
    public static final String RTITLE = "title";
    public static final String RSTATUS = "review_status";
    public static final String APPSTATUS = "app_status";

    public static final String JSON_ARRAY = "result";

    public static final String LOGIN_SUCCESS = "success";

    public static final String SHARED_PREF_NAME = "DoctorBook";

    //This would be used to store the email of current logged in user
    public static final String LOGO_SHARED_PREF = "mailid";
    public static final String UTYPE_SHARED_PREF = "utype";
    public static final String UID_SHARED_PREF = "uid";

    //We will use this to store the boolean in sharedpreference to track user is loggedin or not
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";




}
