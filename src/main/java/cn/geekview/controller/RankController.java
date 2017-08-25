package cn.geekview.controller;

import cn.geekview.aop.LoggerManage;
import cn.geekview.domain.TDreamProduct;
import cn.geekview.service.RedisService;
import cn.geekview.service.TDreamProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.List;
import java.util.Map;

/**
 * 每日增速排行榜前五
 * 分国内国外
 */
@Controller
public class RankController {

    @Autowired
    TDreamProductService productService;

    @LoggerManage(description = "周榜查询")
    @RequestMapping(value = "/week",method = RequestMethod.GET)
    public String queryWeekGrowthSpeedRankTop5(Model model){
        Map maparm = productService.query7DaysPlatformGrowthSpeedRankTop5();
        model.addAttribute("tbweek",maparm.get("tbWeekList"));
        model.addAttribute("jdweek",maparm.get("jdWeekList"));
        model.addAttribute("ksweek",maparm.get("ksWeekList"));
        model.addAttribute("inweek",maparm.get("inWeekList"));
        model.addAttribute("weekBeforeDate",maparm.get("weekBeforeDate"));
        model.addAttribute("nowDate",maparm.get("nowDate"));
        return "week";
    }

    /**
     * 国内每天增速帮前五
     * @param model
     * @return
     */
    @LoggerManage(description = "国内日榜")
    @RequestMapping(value = "/internal",method = RequestMethod.GET )
    public String queryInternalGrowthSpeedRankTop5(Model model){
        Map maparm = productService.queryPlatformGrowthSpeedRankTop5();
        model.addAttribute("tbTop5",maparm.get("tbTop5"));
        model.addAttribute("jdTop5",maparm.get("jdTop5"));
        model.addAttribute("updateDate",maparm.get("updateDate"));
        return "internal";
    }

    /**
     * 国外每天增速帮前五
     * @param model
     * @return
     */
    @LoggerManage(description = "国外日榜")
    @RequestMapping(value = "/overseas",method = RequestMethod.GET )
    public String queryOverseasGrowthSpeedRankTop5(Model model){
        Map maparm = productService.queryPlatformGrowthSpeedRankTop5();
        model.addAttribute("ksTop5",maparm.get("ksTop5"));
        model.addAttribute("inTop5",maparm.get("inTop5"));
        model.addAttribute("updateDate",maparm.get("updateDate"));
        return "overseas";
    }
}
