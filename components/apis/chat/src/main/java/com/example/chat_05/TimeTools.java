package com.example.chat_05;


import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

public class TimeTools {


  @Tool(description = "Returns the current time in a specific time for an IANA time zone identifier")
  public String currentTimeIn(@ToolParam(required = false, description = "IANA time zone identifiers") String timeZone) {
    ZoneId zoneId = ZoneId.of("Europe/Paris");
    ZonedDateTime now = ZonedDateTime.now(zoneId);
    return now.format(DateTimeFormatter.ISO_LOCAL_TIME);
  }
}
