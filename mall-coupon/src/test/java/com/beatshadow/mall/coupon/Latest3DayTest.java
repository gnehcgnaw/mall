package com.beatshadow.mall.coupon;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.util.Date;

/**
 * @author : <a href="mailto:gnehcgnaw@gmail.com">gnehcgnaw</a>
 * @since : 2020/5/30 21:51
 */
@Slf4j
public class Latest3DayTest {

    @Test
    public void test(){
        log.debug("最近三天的时间间隔是：{}——{}",startTime(),endTime());
    }


    private String startTime(){
        LocalDate now = LocalDate.now();
        LocalTime min = LocalTime.MIN;
        LocalDateTime startTime = LocalDateTime.of(now,min);
        ZoneId zoneId = ZoneOffset.systemDefault();
        long time = Date.from(LocalDateTime.now().atZone(zoneId).toInstant()).getTime();
        System.out.println(time);
        System.out.println(System.currentTimeMillis());
        return startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
    }

    private String  endTime(){
        LocalDate localDate = LocalDate.now().plusDays(2);
        LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
    }

    @Test
    public void  currentTime(){
        log.debug("current time is {}",System.currentTimeMillis());
    }

}
