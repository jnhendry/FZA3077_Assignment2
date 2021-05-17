package com.hotmail.jamesnhendry.fza3077;


import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateAge {

    long timestamp;

    public DateAge(){

    }

    public DateAge(long aTimestamp){
        timestamp = aTimestamp;
    }

    public int getAge(){

        long now = System.currentTimeMillis();
        double difference = now - this.timestamp;
        System.out.println( " Dob" + this.timestamp);
        System.out.println(now);

        int age = (int) Math.floor(((((difference/1000)/60)/60)/8160)) - 2 ;

        return  age;


    }

    public String getDateOfBirth(){

        return new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH).format(this.timestamp);
    }
}
