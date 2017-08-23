package cn.geekview.service.impl;

import cn.geekview.domain.TDreamProduct;
import cn.geekview.mapper.TDreamProductMapper;
import cn.geekview.service.RedisService;
import cn.geekview.service.TDreamProductService;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("TDreamProductServiceImpl")
public class TDreamProductServiceImpl implements TDreamProductService {

    private DateTime date = new DateTime(DateTime.now().getYear(),DateTime.now().getMonthOfYear(),DateTime.now().getDayOfMonth(),12,0,0);

    @Autowired
    private TDreamProductMapper productMapper;

    @Autowired
    private RedisService redisService;

    /**
     * 七天数据查询
     * @return
     */
    @Override
    public Map<String, List> query7DaysPlatformGrowthSpeedRankTop5() {
        Map table = new HashMap();
        Map maParm = new HashMap();
        table.put("tb","t_dream_tb_project");
        table.put("jd","t_dream_jd_project");
        table.put("ks","t_dream_ks_project");
        table.put("in","t_dream_in_project");
        table.forEach((key, value) -> {
//            System.out.println(key+":"+value);
            List<TDreamProduct> list = new ArrayList<>();
            //查询日期一周前开始众筹的
            List<TDreamProduct> oldlist = productMapper.query7DaysOldpeojectsRankTop5(date.toDate(),date.plusDays(-7).toDate(),String.valueOf(value));
            //查询日期一周内新上的
            List<TDreamProduct> newlist = productMapper.query7DaysNewpeojectsRankTop5(date.plusDays(1).toDate(),date.plusDays(-7).toDate(),String.valueOf(value));
            list.addAll(oldlist);
            list.addAll(newlist);
            if (list != null&&list.size()>0) {
                for (TDreamProduct product : list) {
                    if (product!=null){
                        BigDecimal currencyExchange =  redisService.getCurrencyExchange(product.getMoneyCurrency());
                        BigDecimal growthMoneyCNY =  currencyExchange.multiply(product.getGrowthMoney());
                        product.setGrowthMoney(growthMoneyCNY);
                    }
                }
            }
            Collections.sort(list,new Comparator<TDreamProduct>() {
                @Override
                public int compare(TDreamProduct o1, TDreamProduct o2) {//默认是升序排列，如果要降序，将对象互换即可
                    return o2.getGrowthMoney().compareTo(o1.getGrowthMoney());
                }
            });
            maParm.put(key+"WeekList",list);
        });
        return maParm;
    }



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
        return null;
    }



}
