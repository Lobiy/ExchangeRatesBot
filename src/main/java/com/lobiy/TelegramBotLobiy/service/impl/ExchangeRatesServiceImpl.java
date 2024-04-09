package com.lobiy.TelegramBotLobiy.service.impl;

import com.lobiy.TelegramBotLobiy.Exception.ServiceException;
import com.lobiy.TelegramBotLobiy.client.CbrClient;
import com.lobiy.TelegramBotLobiy.service.ExchangeRatesService;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.xml.sax.InputSource;

import org.w3c.dom.Document;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;
import org.slf4j.Logger;
@Service
public class ExchangeRatesServiceImpl implements ExchangeRatesService {
    @Autowired
    private CbrClient client;

    private static final Logger LOG = LoggerFactory.getLogger(ExchangeRatesServiceImpl.class);
    private static final String USD_XPATH = "/ValCurs//Valute[@ID='R01235']/Value";
    private static final String EUR_XPATH = "/ValCurs//Valute[@ID='R01239']/Value";

    @Cacheable(value = "usd", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getUSDExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXml(xml, USD_XPATH);
    }

    @CacheEvict("usd")
    public void clearUsdCache() {
        LOG.info("Clearing USD cache");
    }

    @Cacheable(value = "eur", unless = "#result == null or #result.isEmpty()")
    @Override
    public String getEURExchangeRate() throws ServiceException {
        var xml = client.getCurrencyRatesXML();
        return extractCurrencyValueFromXml(xml, EUR_XPATH);
    }

    @CacheEvict("eur")
    public void clearEurCache() {
        LOG.info("Clearing EUR cache");
    }

    private static String extractCurrencyValueFromXml(String xml, String xpathExpression) throws ServiceException {
        InputSource source = new InputSource(new StringReader(xml));
        try{
            XPath xPath = XPathFactory.newInstance().newXPath();
            var document = (Document) xPath.evaluate("/", source, XPathConstants.NODE);
            return xPath.evaluate(xpathExpression, document);
        } catch (XPathExpressionException e) {
            throw new ServiceException("Can't parse XML", e);
        }
    }
}
