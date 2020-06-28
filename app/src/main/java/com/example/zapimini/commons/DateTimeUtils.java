package com.example.zapimini.commons;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimeUtils {
    SimpleDateFormat sdf;

    public String getTodayDate(){
        sdf = new SimpleDateFormat("YYYY-MM-dd");
        return sdf.format(new Date());
    }

    public String getTodayDateTime(){
        sdf = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public String getYesterdayDate(){
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);

        sdf = new SimpleDateFormat("YYYY-MM-dd");
        return sdf.format(cal.getTime());
    }

    public String removeTimeInDateTime(String dateTime){
        String[] reDated = dateTime.split(" ");
        return reDated[0];
    }

    public String convertDateToLabel(String date){
        String label = null;

        if(getTodayDate().equals(date)){
            label = "Today";
        }else if(getYesterdayDate().equals(date)){
            label = "Yesterday";
        }else{
            label = date;
        }
        return label;
    }
}
