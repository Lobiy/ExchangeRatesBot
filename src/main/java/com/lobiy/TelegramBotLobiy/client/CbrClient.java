package com.lobiy.TelegramBotLobiy.client;

import com.lobiy.TelegramBotLobiy.Exception.ServiceException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CbrClient {

    @Autowired
    public OkHttpClient client;

    @Value("${cbr.currency.rates.xml.url}")
    private String url;

    public String getCurrencyRatesXML() throws ServiceException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response =  client.newCall(request).execute()) {
            ResponseBody body = response.body();
            return body == null ? null : body.string();
        } catch (IOException e) {
             throw new ServiceException("Error on getting currency rates", e);
        }
    }
}
