package cn.geekview.controller;

import cn.geekview.service.TDreamProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.Map;

@Controller
public class RankController {

    @Autowired
    TDreamProductService productService;

    @RequestMapping(value = "/top5",method = RequestMethod.GET )
    public String queryPlatformGrowthSpeedRankTop5(Model model){

        Map maparm = productService.queryPlatformGrowthSpeedRankTop5();
        model.addAttribute("tbTop5",maparm.get("tbTop5"));
        model.addAttribute("jdTop5",maparm.get("jdTop5"));
        model.addAttribute("ksTop5",maparm.get("ksTop5"));
        model.addAttribute("inTop5",maparm.get("inTop5"));
        model.addAttribute("updateDate",maparm.get("updateDate"));
        return "rank";
    }
}
