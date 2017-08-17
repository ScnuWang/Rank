package cn.geekview.controller;

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


    /**
     * 国内增速帮前五
     * @param model
     * @return
     */
    @RequestMapping(value = "/internal",method = RequestMethod.GET )
    public String queryInternalGrowthSpeedRankTop5(Model model){
        Map maparm = productService.queryPlatformGrowthSpeedRankTop5();
        model.addAttribute("tbTop5",maparm.get("tbTop5"));
        model.addAttribute("jdTop5",maparm.get("jdTop5"));
        model.addAttribute("updateDate",maparm.get("updateDate"));
        return "internal";
    }

    /**
     * 国外增速帮前五
     * @param model
     * @return
     */
    @RequestMapping(value = "/overseas",method = RequestMethod.GET )
    public String queryOverseasGrowthSpeedRankTop5(Model model){
        Map maparm = productService.queryPlatformGrowthSpeedRankTop5();
        model.addAttribute("ksTop5",maparm.get("ksTop5"));
        model.addAttribute("inTop5",maparm.get("inTop5"));
        model.addAttribute("updateDate",maparm.get("updateDate"));
        return "overseas";
    }
}
