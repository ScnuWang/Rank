package cn.geekview.service.impl;

import cn.geekview.domain.TDreamProduct;
import cn.geekview.mapper.TDreamProductMapper;
import cn.geekview.service.RedisService;
import cn.geekview.service.TDreamProductService;
import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service("TDreamProductServiceImpl")
public class TDreamProductServiceImpl implements TDreamProductService {

    protected Logger logger = Logger.getLogger(this.getClass());

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
        table.forEach((Object key, Object value) -> {
            List<TDreamProduct> alllist = new ArrayList<>();
            //查询日期一周前开始众筹的
            logger.info(key+"从Redis中查询日期一周前开始众筹的产品");
            List<TDreamProduct> oldlist = (List<TDreamProduct>) redisService.getObject(key+"oldlist");
            if (oldlist==null||oldlist.size()==0){
                logger.info(key+"从Mysql中查询日期一周前开始众筹的产品");
                oldlist = productMapper.query7DaysOldpeojectsRankTop5(date.toDate(),date.plusDays(-7).toDate(),String.valueOf(value));
                redisService.set(key+"oldlist",oldlist);
                redisService.expire(key+"newlist",2*60*60);
            }
            //查询日期一周内新上的
            logger.info(key+"从Redis中查询日期一周内新上的产品");
            List<TDreamProduct> newlist = (List<TDreamProduct>) redisService.getObject(key+"newlist");
            if (newlist==null||newlist.size()==0){
                logger.info(key+"从Mysql中查询日期一周内新上的产品");
                newlist = productMapper.query7DaysNewpeojectsRankTop5(date.plusDays(1).toDate(),date.plusDays(-7).toDate(),String.valueOf(value));
                redisService.set(key+"newlist",newlist);
                redisService.expire(key+"newlist",2*60*60);
            }
            alllist.addAll(oldlist);
            alllist.addAll(newlist);
            //换算
            if (alllist != null&&alllist.size()>0) {
                for (TDreamProduct product : alllist) {
                    if (product!=null){
                        BigDecimal currencyExchange =  redisService.getCurrencyExchange(product.getMoneyCurrency());
                        BigDecimal growthMoneyCNY =  currencyExchange.multiply(product.getGrowthMoney());
                        product.setGrowthMoney(growthMoneyCNY);
                        product.setMoneyCurrency("CNY");
                    }
                }
                //排序
                Collections.sort(alllist, (o1, o2) -> {//默认是升序排列，如果要降序，将对象互换即可
                    return o2.getGrowthMoney().compareTo(o1.getGrowthMoney());
                });
                //去重
                Set<TDreamProduct> set = new HashSet<>();
                for (TDreamProduct product : alllist) {
                    set.add(product);
                }
                List<TDreamProduct> resultlist = new ArrayList<>(set);
                //排序
                Collections.sort(resultlist, (o1, o2) -> {//默认是升序排列，如果要降序，将对象互换即可
                    return o2.getGrowthMoney().compareTo(o1.getGrowthMoney());
                });
                maParm.put(key+"WeekList",resultlist.subList(0,5));
            }
        });
        logger.info("当前时间："+date.toDate());
        maParm.put("nowDate",date.toDate());
        maParm.put("weekBeforeDate",date.plusDays(-7).toDate());
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
