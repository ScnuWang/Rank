package cn.geekview;

import cn.geekview.mapper.TDreamProductMapper;
import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DatasourceTest {

    @Autowired
    private TDreamProductMapper productMapper;


    @Test
    public void test(){
        DateTime dateTime = DateTime.now();
        DateTime date = new DateTime(dateTime.getYear(),dateTime.getMonthOfYear(),dateTime.getDayOfMonth(),12,0,0);
        System.out.println(productMapper.queryPlatformGrowthSpeedRankTop5(dateTime.toDate(),1));

    }

    @Test
    public void test2(){
        DateTime dateTime = new DateTime(2017,8,11,12,0,0);
        System.out.println(productMapper.queryAllGrowthSpeedRank(dateTime.toDate()).size());

    }


}
