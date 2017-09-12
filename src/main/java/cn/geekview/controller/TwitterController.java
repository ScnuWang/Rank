package cn.geekview.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.twitter.api.SearchResults;
import org.springframework.social.twitter.api.Tweet;
import org.springframework.social.twitter.api.Twitter;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TwitterController {
    @Autowired
    private Twitter twitter;

    @RequestMapping("/twitter")
    public String twitter(){
        return "searchPage";
    }


    @RequestMapping("/search")
    public String helloTwitter(@RequestParam(defaultValue ="Jason W")String search, Model model){
        SearchResults results = twitter.searchOperations().search(search);
//        String text = results.getTweets().get(0).getText();
//        model.addAttribute("message",text);
//        List<String> tweets  = results.getTweets().stream().map(Tweet::getText).collect(Collectors.toList());
        List<Tweet> tweets = results.getTweets();
        model.addAttribute("tweets",tweets);
        model.addAttribute("search",search);
        return "twitter";
    }

}
