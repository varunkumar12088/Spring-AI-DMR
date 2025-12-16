package com.learning.ai.tools;

import com.learning.ai.util.LogUtil;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.i18n.LocaleContextHolder;

import java.time.LocalDateTime;

public class SimpleDateTimeTool {

    // Information tools
    @Tool(description = "Get the current date and time in users zone.")
    public String getCurrentDateTime(){
        LogUtil.log("calling getCurrentDateTime");
        return LocalDateTime.now()
                .atZone(LocaleContextHolder.getTimeZone().toZoneId())
                .toString();
    }
}
