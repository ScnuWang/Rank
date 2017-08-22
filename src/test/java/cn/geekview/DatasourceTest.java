package cn.geekview;

import cn.geekview.domain.TDreamCurrency;
import cn.geekview.domain.TDreamProduct;
import cn.geekview.mapper.TDreamCurrencyMapper;
import cn.geekview.mapper.TDreamProductMapper;
import cn.geekview.service.RedisService;
import cn.geekview.service.TDreamProductService;
import com.xiaoleilu.hutool.util.BeanUtil;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DatasourceTest {

    @Autowired
    private TDreamProductMapper productMapper;

    @Autowired
    private TDreamCurrencyMapper currencyMapper;

    @Autowired
    private RedisService redisService;

    @Autowired
    private TDreamProductService productService;

    @Test
    public void test6(){
        for (TDreamCurrency currency : currencyMapper.queryCurrency()) {
            System.out.println(BeanUtil.beanToMap(currency).get("currencyNick"));
        }
    }


    @Test
    public void test5(){
        Map currencyMap = (Map) redisService.getObject("currency");
        if(currencyMap==null||currencyMap.size()==0){
            List<TDreamCurrency> currencyList = currencyMapper.queryCurrency();
            Map maParm = new HashMap();
            for (TDreamCurrency currency : currencyList) {
                maParm.put(currency.getCurrencyNick(),currency.getCurrencyExchange());
            }
            currencyMap = maParm;
            redisService.set("currency",maParm);
        }
        System.out.println(currencyMap.get("USD"));
    }


    @Test
    public void test4(){
        productService.query7DaysPlatformGrowthSpeedRankTop5();
    }

    @Test
    public void test3() {
        DateTime dateTime = DateTime.now();
        DateTime nowdate = new DateTime(dateTime.getYear(),dateTime.getMonthOfYear(),15,12,0,0);
        DateTime weekdate = new DateTime(dateTime.getYear(),dateTime.getMonthOfYear(),nowdate.getDayOfMonth()-7,12,0,0);
        System.out.println(productMapper.query7DaysNewpeojectsRankTop5(nowdate.plusDays(1).toDate(),weekdate.toDate(),"t_dream_jd_project"));
    }

    @Test
    public void test2() {
        DateTime dateTime = DateTime.now();
        DateTime nowdate = new DateTime(dateTime.getYear(),dateTime.getMonthOfYear(),15,12,0,0);
        DateTime weekdate = new DateTime(dateTime.getYear(),dateTime.getMonthOfYear(),nowdate.getDayOfMonth()-7,12,0,0);
        System.out.println(productMapper.query7DaysOldpeojectsRankTop5(nowdate.toDate(),weekdate.toDate(),"t_dream_jd_project"));
    }



    @Test
    public void test1() throws InterruptedException {
        DateTime dateTime = DateTime.now();
        DateTime date = new DateTime(dateTime.getYear(),dateTime.getMonthOfYear(),15,12,0,0);
        List<TDreamProduct> tblist = productMapper.queryPlatformGrowthSpeedRankTop5(date.toDate(),1);
        redisService.set("tblist",tblist);
        System.out.println("存入Redis："+redisService.getObject("tblist"));
        //设置有效时间
        redisService.expire("tblist",30);
        Thread.sleep(40*1000);
        System.out.println("取出Redis："+redisService.getObject("tblist"));

    }

    @Test
    public void test0(){
        DateTime dateTime = new DateTime(2017,8,11,12,0,0);
        System.out.println(productMapper.queryAllGrowthSpeedRank(dateTime.toDate()).size());

    }


}
