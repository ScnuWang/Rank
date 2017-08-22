package cn.geekview.service;

import cn.geekview.domain.TDreamProduct;

import java.util.List;
import java.util.Map;

public interface TDreamProductService {

    Map<String,List> queryPlatformGrowthSpeedRankTop5();

    List<TDreamProduct> queryAllGrowthSpeedRank();

    Map<String,List> query7DaysPlatformGrowthSpeedRankTop5();
}
