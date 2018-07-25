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


    public static String appointListTimestampMonDate(String date) {


        try {
            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");


            Date newDate = spf.parse(date);

            spf= new SimpleDateFormat("MMM dd");
            date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            date = "error";
        }


        return date;
    }

    public static String appointListTimestampYear(String date) {



        try {


            SimpleDateFormat spf=new SimpleDateFormat("yyyy-MM-dd");


            Date  newDate = spf.parse(date);

            spf= new SimpleDateFormat("yyyy");
            date = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
            date = "error";
        }


        return date;
    }

    public static long milliseconds(String date)
    {
        //String date_ = date;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try
        {
            Date mDate = sdf.parse(date);
            long timeInMilliseconds = mDate.getTime();
            System.out.println("Date in milli :: " + timeInMilliseconds);
            return timeInMilliseconds;
        }
        catch (ParseException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return 0;
    }


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


    public static String hour24to12hour(String time)
    {
        try {
            final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            final Date dateObj = sdf.parse(time);
            //DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            return new SimpleDateFormat("hh:mm a").format(dateObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }


    public static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

}
