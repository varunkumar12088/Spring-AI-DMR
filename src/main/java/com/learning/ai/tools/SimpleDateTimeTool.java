package com.learning.ai.tools;

import com.learning.ai.util.LogUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SimpleDateTimeTool {

    // Information tools
    @Tool(description = "Get the current date and time in users zone.")
    public String getCurrentDateTime(){
        LogUtil.log("calling getCurrentDateTime");
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }

    //Action tool
    @Tool(description = "Set the alarm for given time.")
    public void setAlarm(@ToolParam(description = "Time in ISO-8601 format") String time){
        var dateTime = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME);
        LogUtil.log("calling setAlarm :"  + dateTime);
    }
}
