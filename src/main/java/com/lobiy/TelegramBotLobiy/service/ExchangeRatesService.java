package com.lobiy.TelegramBotLobiy.service;

import com.lobiy.TelegramBotLobiy.Exception.ServiceException;

public interface ExchangeRatesService {

    String getUSDExchangeRate() throws ServiceException;

    String getEURExchangeRate() throws ServiceException;
}
