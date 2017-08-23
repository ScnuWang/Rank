package cn.geekview.service;

import java.math.BigDecimal;

public interface RedisService {

    void set(String key,String value);

    void set(String key,Object object);

    String get(String key);

    Object getObject(String key);

    void delete(String key);

    boolean expire(String key,long timeout);

    BigDecimal getCurrencyExchange(String currencyNick);
}
