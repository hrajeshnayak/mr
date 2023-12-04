package Util;

import java.util.Random;

public class Constants {

    public static final String URL_UDTL = "https://www.prathigram.com/getuaddretdetail.php?mphone=";
    public static final String URL_UFDTL = "https://www.prathigram.com/getretcatdetail.php?retcode=";
    public static final String URL_CDTL = "https://www.prathigram.com/getproddetail.php?catcode=";
    public static final String URL_SRCHPROD = "https://www.prathigram.com/getsproddetail.php?retcode=";
    public static final String URL_SRCHPRODRT = "&search=";
    public static final String URL_PRODDTL = "https://www.prathigram.com/getitemdetails.php?retcode=";
    public static final String URL_PRODDTLRT = "&prodcode=";
    public static final String URL_CARTDTL = "https://www.prathigram.com/getcartdetail.php?ucode=";
    public static final String URL_CARTDTLS = "https://www.prathigram.com/getcartdetails.php?ucode=";
    public static final String URL_CARTEDTQTY = "https://www.prathigram.com/editcartqty.php";
    public static final String URL_CARTDELITEM = "https://www.prathigram.com/delcartitem.php?listid=";
    public static final String URL_CARTITEMDTL = "https://www.prathigram.com/cartitemdetail.php?listid=";
    public static final String URL_CARTCHKOUT = "https://www.prathigram.com/cartchkout.php?ucode=";
    public static final String URL_PDTL = "https://www.prathigram.com/getprofile.php?mphone=";
    public static final String URL_OTPVERIFIED = "https://www.prathigram.com/otpstatus.php?ucode=";
    public static final String URL_ORDERDTLS = "https://www.prathigram.com/getorderdetails.php?ucode=";
    public static final String URL_ORDERLIST = "https://www.prathigram.com/getorderlist.php?cartId=";


    public static final String API_KEY = "&apikey=199b1542";
    public static final String KEY_NAME = "name";
    public static final String KEY_RETNAME = "retname";
    public static final String KEY_RETPHONE = "retphone";
    public static final String KEY_RETADD1 = "retadd1";
    public static final String KEY_RETCITY = "retcity";
    public static final String KEY_RETIMAGE = "retimage";
    public static final String KEY_RETDESC = "retdesc";
    public static final String JSON_ARRAY = "result";

    public static int randomInt(int max, int min) {
        return new Random().nextInt(max - min) + min;

    }


}
