package cn.geekview.service.impl;

import cn.geekview.domain.TDreamProduct;
import cn.geekview.mapper.TDreamProductMapper;
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
        Map maparm = new HashMap<String,List>();
        List internalList = new ArrayList();
        List overseasList = new ArrayList();
        //这里需要优化一下
        List<TDreamProduct> tbList = productMapper.queryPlatformGrowthSpeedRankTop5(date,1);
        List<TDreamProduct> jdList = productMapper.queryPlatformGrowthSpeedRankTop5(date,2);
        List<TDreamProduct> ksList = productMapper.queryPlatformGrowthSpeedRankTop5(date,3);
        List<TDreamProduct> inList = productMapper.queryPlatformGrowthSpeedRankTop5(date,4);

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
