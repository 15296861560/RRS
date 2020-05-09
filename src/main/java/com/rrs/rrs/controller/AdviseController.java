package com.rrs.rrs.controller;

import com.rrs.rrs.mapper.AdviseMapper;
import com.rrs.rrs.model.Advise;
import com.rrs.rrs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdviseController {
    @Autowired
    AdviseMapper adviseMapper;

    @GetMapping("/advise")
    public String index(Model model){
        model.addAttribute("title","title");
        model.addAttribute("description","title");
        model.addAttribute("error","error");
        return "advise";
    }

    //提交投诉或建议
    @PostMapping("/advise/submit")
    public String doPublish(
            @RequestParam(value = "title",required = false)String title,
            @RequestParam(value ="description",required = false)String description,
            @RequestParam(value ="advice_type",required = false)String advice_type,
            HttpServletRequest request,
            Model model){
        User user=(User)request.getSession().getAttribute("user");

        Advise advise=new Advise();
        advise.setTitle(title);
        advise.setDescription(description);
        advise.setAdviceType(advice_type);
        advise.setGmtCreate(System.currentTimeMillis());
        advise.setCreator(user.getUserId());
        adviseMapper.createAdvise(advise);
        model.addAttribute("tip","提交成功");
        model.addAttribute("src","/food");
        return "tip";
    }


}
