package cn.geekview.service.impl;

import cn.geekview.domain.TDreamProduct;
import cn.geekview.mapper.TDreamProductMapper;
import cn.geekview.service.RedisService;
import cn.geekview.service.TDreamProductService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("TDreamProductServiceImpl")
public class TDreamProductServiceImpl implements TDreamProductService {

    private Date date = new DateTime(DateTime.now().getYear(),DateTime.now().getMonthOfYear(),DateTime.now().getDayOfMonth()-1,12,0,0).toDate();

    @Autowired
    private TDreamProductMapper productMapper;


    //第一次查询放入redis中，后面直接从redis中取值
    @Override
    public Map<String,List> queryPlatformGrowthSpeedRankTop5() {
        //如果当天的数据还没有完全产生，则展示前一天的
        Date date = new DateTime(DateTime.now().getYear(),DateTime.now().getMonthOfYear(),DateTime.now().getDayOfMonth(),12,0,0).toDate();
        Map maparm = new HashMap<String,List>();
        //这里需要优化一下
        List<TDreamProduct> tbList = productMapper.queryPlatformGrowthSpeedRankTop5(date,1);
        List<TDreamProduct> jdList = productMapper.queryPlatformGrowthSpeedRankTop5(date,2);
        List<TDreamProduct> ksList = productMapper.queryPlatformGrowthSpeedRankTop5(date,3);
        List<TDreamProduct> inList = productMapper.queryPlatformGrowthSpeedRankTop5(date,4);

        if(tbList.size()==0||jdList.size()==0||ksList.size()==0||inList.size()==0){
            date = new DateTime(DateTime.now().getYear(),DateTime.now().getMonthOfYear(),DateTime.now().getDayOfMonth()-1,12,0,0).toDate();
            tbList = productMapper.queryPlatformGrowthSpeedRankTop5(date,1);
            jdList = productMapper.queryPlatformGrowthSpeedRankTop5(date,2);
            ksList = productMapper.queryPlatformGrowthSpeedRankTop5(date,3);
            inList = productMapper.queryPlatformGrowthSpeedRankTop5(date,4);
        }

        maparm.put("tbTop5",tbList);
        maparm.put("jdTop5",jdList);
        maparm.put("ksTop5",ksList);
        maparm.put("inTop5",inList);
        maparm.put("updateDate",date);

        return maparm;
    }

    @Override
    public List<TDreamProduct> queryAllGrowthSpeedRank() {
        //加一个分页
        return productMapper.queryAllGrowthSpeedRank(date);
    }

}
