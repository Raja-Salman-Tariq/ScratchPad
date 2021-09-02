package com.example.a3v2.db

import android.util.Log
import java.util.*

class MyTimestampFormatter(timestamp: String, ){
    val formattedTimestamp : String
    init{

        Log.d("mytsfrmtr", ": $timestamp")
        val strTokr =   StringTokenizer(timestamp, "- :")
        var string  =   ""
        val yr      =   strTokr.nextToken()
        val month   =   strTokr.nextToken()
        val day     =   strTokr.nextToken()

        var hour    =   strTokr.nextToken()
        val min     =   strTokr.nextToken()

        var timeOfDay = "A.M.";

        if (hour.toInt()>12){
            timeOfDay="P.M."
            hour=(hour.toInt()-12).toString()
        }

        formattedTimestamp = day+" "+toMonth(month)+" "+yr+", "+hour+":"+min+" "+timeOfDay
        Log.d("mytsfrmtr", "formatted: : $formattedTimestamp")

    }

    private fun toMonth(str:String): String {
        return when (str){
            "01"    ->  "Jan"
            "02"    ->  "Feb"
            "03"    ->  "Mar"
            "04"    ->  "Apr"
            "05"    ->  "May"
            "06"    ->  "Jun"
            "07"    ->  "Jul"
            "08"    ->  "Aug"
            "09"    ->  "Sep"
            "10"    ->  "Oct"
            "11"    ->  "Nov"
            else    ->  "Dec"
        }
    }
}