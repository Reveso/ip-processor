package com.lukasrosz.ipprocessor.processing.application.scheduler;

import com.lukasrosz.ipprocessor.processing.application.AddressProcessor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AddressProcessingScheduler {

    private final AddressProcessor addressProcessor;

    @Scheduled(fixedDelayString = "${task.ipprocessing.fixedDelay.inMiliseconds}",
            initialDelayString = "${task.ipprocessing.initialDelay.inMiliseconds}")
    public void performIpProcessing() {
        log.info("Performing ip processing");
        addressProcessor.processAddresses();
        log.info("Ip processing finished");
    }
}
