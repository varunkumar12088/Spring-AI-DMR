package com.learning.ai.tools;

import com.learning.ai.properties.AIProperties;
import com.learning.ai.util.LogUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class WeatherTool {

    private final AIProperties  aiProperties;
    private final RestClient restClient;

    @Tool(description = "Get weather information of given city")
    public String getWeather(@ToolParam(description = "City of which we want to get weather information") String city){
        LogUtil.log("getWeather calling ");
        // external api call to get weather information
        // RestTemplate, RestClient
        var response = restClient
                .get()
                .uri(uriBuilder -> uriBuilder.path("/current.json")
                        .queryParam("key", aiProperties.weather().apiKey())
                        .queryParam("q", city)
                        .build()
                )
                .retrieve()
                .body(String.class);
        LogUtil.log("getWeather called ");
        LogUtil.log("getWeather response is " + response);
        return response;
    }
}
