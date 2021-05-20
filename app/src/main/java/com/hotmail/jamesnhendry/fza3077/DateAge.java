package com.hotmail.jamesnhendry.fza3077;

import java.text.SimpleDateFormat;
import java.util.Locale;

// This class is used to convert dates in the form of timestamps to meaningful dates for the system.
public class DateAge {
    //test

    long timestamp;

    public DateAge(){
    }

    public DateAge(long aTimestamp){
        timestamp = aTimestamp;
    }

    //This method returns the age of a user based on the timestamp value that the object was created with.
    public int getAge(){
        long now = System.currentTimeMillis();
        double difference = now - this.timestamp;
        System.out.println( " Dob" + this.timestamp);
        System.out.println(now);

        int age = (int) Math.floor(((((difference/1000)/60)/60)/8160)) - 2 ;
        return  age;
    }

    //This method is used to get back the date of birth that will be generated from the timestamp it was created with.
    public String getDateOfBirth(){
        return new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(this.timestamp);
    }
}
