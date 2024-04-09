package com.lobiy.TelegramBotLobiy.scheduler;

import com.lobiy.TelegramBotLobiy.service.impl.ExchangeRatesServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class InvalidationScheduler {

    @Autowired
    private ExchangeRatesServiceImpl service;

    @Scheduled(cron = "* 0 0 * * ?", zone = "Europe/Moscow")
    public void invalidateCache() {
        service.clearUsdCache();
        service.clearEurCache();
    }
}
