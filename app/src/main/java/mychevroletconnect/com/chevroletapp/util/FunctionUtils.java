package mychevroletconnect.com.chevroletapp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author pocholomia
 * @since 13/09/2016
 */
public class FunctionUtils {

    public static final String FULL_23_HR_DATE = "yyyy-MM-dd";
    public static final String DATE_ONLY = "MMM dd, yyyy";
    public static final String DATE_NUM_ONLY = "MM.dd.yyyy";



    public static String toReadable(String dateToConvert){
        String convertedDate;
        //String[] arr = dateToConvert.split(" ");
        DateFormat targetFormat = new SimpleDateFormat(FULL_23_HR_DATE,Locale.US);
        Date date = null;
        try {
            date = targetFormat.parse(dateToConvert);


            SimpleDateFormat formatter = new SimpleDateFormat(DATE_ONLY,Locale.US);
            convertedDate = formatter.format(date);
            return convertedDate.toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    public static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

}
