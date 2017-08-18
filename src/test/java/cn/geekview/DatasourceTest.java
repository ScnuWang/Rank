package cn.geekview;

import cn.geekview.domain.TDreamProduct;
import cn.geekview.mapper.TDreamProductMapper;
import cn.geekview.service.RedisService;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DatasourceTest {

    @Autowired
    private TDreamProductMapper productMapper;

    @Autowired
    private RedisService redisService;

    @Test
    public void test() throws InterruptedException {
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
    public void test2(){
        DateTime dateTime = new DateTime(2017,8,11,12,0,0);
        System.out.println(productMapper.queryAllGrowthSpeedRank(dateTime.toDate()).size());

    }


}
