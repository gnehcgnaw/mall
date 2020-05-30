package com.beatshadow.mall.coupon;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
        return startTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
    }

    private String  endTime(){
        LocalDate localDate = LocalDate.now().plusDays(2);
        LocalDateTime endTime = LocalDateTime.of(localDate, LocalTime.MAX);
        return endTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) ;
    }

}
