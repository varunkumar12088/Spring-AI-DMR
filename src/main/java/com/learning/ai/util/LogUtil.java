package com.learning.ai.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.ObjectUtils;

public class LogUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void log(Object object) {
        try{
            if(ObjectUtils.allNotNull(object)){
                if(isAnyTypeOf(object, String.class, Number.class)){
                    System.out.println(object);
                    return;
                }
                String json = objectMapper
                        .writerWithDefaultPrettyPrinter()
                        .writeValueAsString(object);
                System.out.println(json);
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }

    }

    private static boolean isAnyTypeOf(Object object, Class<?>... types) {
        for (Class<?> type : types) {
            if(type.isInstance(object)){
                return true;
            }
        }
        return false;
    }
}
