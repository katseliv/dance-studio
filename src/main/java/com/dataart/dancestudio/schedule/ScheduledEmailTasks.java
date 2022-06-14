package com.dataart.dancestudio.schedule;

import com.dataart.dancestudio.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Component
public class ScheduledEmailTasks {

    private final BookingService bookingService;

    @Scheduled(cron = "00 00 11-22 * * *") // From 11:00 to 22:00 every day
    public void findComingLessons() {
        log.info("Coming lessons search launched at {}.", LocalDateTime.now());
        bookingService.findAndNotifyByStartingInHours(1);
        log.info("Coming lessons search is finished.");
    }

}
