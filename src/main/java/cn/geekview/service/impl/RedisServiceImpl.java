package cn.geekview.service.impl;

import cn.geekview.domain.TDreamCurrency;
import cn.geekview.mapper.TDreamCurrencyMapper;
import cn.geekview.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
@Service("RedisServiceImpl")
public class RedisServiceImpl implements RedisService{

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private TDreamCurrencyMapper currencyMapper;

    @Override
    public void set(String key, String value) {
        stringRedisTemplate.opsForValue().set(key, value);
    }

    @Override
    public void set(String key, Object object) {
        redisTemplate.opsForValue().set(key,object);
    }

    @Override
    public String get(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

    @Override
    public Object getObject(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public boolean expire(String key, long timeout) {
        return redisTemplate.expire(key,timeout, TimeUnit.SECONDS);
    }

    @Override
    public BigDecimal getCurrencyExchange(String currencyNick) {
        Map currencyMap = (Map) this.getObject("currency");
        if(currencyMap==null||currencyMap.size()==0){
            List<TDreamCurrency> currencyList = currencyMapper.queryCurrency();
            Map maParm = new HashMap();
            for (TDreamCurrency currency : currencyList) {
                maParm.put(currency.getCurrencyNick(),currency.getCurrencyExchange());
            }
            currencyMap = maParm;
            this.set("currency",maParm);
        }
        return (BigDecimal) currencyMap.get(currencyNick);
    }
}
