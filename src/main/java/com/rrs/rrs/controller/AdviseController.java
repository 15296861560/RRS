package com.rrs.rrs.controller;

import com.rrs.rrs.dto.PageDTO;
import com.rrs.rrs.mapper.AdviseMapper;
import com.rrs.rrs.model.Advise;
import com.rrs.rrs.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class AdviseController {
    @Autowired
    AdviseMapper adviseMapper;

    @GetMapping("/advise")
    public String advise(Model model){
        model.addAttribute("title","标题");
        model.addAttribute("description","描述");
        model.addAttribute("advise_type","建议");
        model.addAttribute("action","advise");
        model.addAttribute("nav","advise");
        return "advise";
    }

    //提交投诉或建议
    @PostMapping("/advise/submit")
    public String doAdvise(
            @RequestParam(value = "title",required = false)String title,
            @RequestParam(value ="description",required = false)String description,
            @RequestParam(value ="advise_type",required = false)String advise_type,
            HttpServletRequest request,
            Model model){
        User user=(User)request.getSession().getAttribute("user");

        if (user==null){
            return "redirect:/noLogin";
        }

        Advise advise=new Advise();
        advise.setTitle(title);
        advise.setDescription(description);
        advise.setAdviseType(advise_type);
        advise.setGmtCreate(System.currentTimeMillis());
        advise.setCreator(user.getUserId());
        adviseMapper.createAdvise(advise);
        model.addAttribute("tip","提交成功");
        model.addAttribute("src","/food");
        return "tip";
    }

    @GetMapping("/checkAdvise")
    public String checkAdvise(Model model,
                              @RequestParam(name="page",defaultValue = "1")Integer page,
                              @RequestParam(name="size",defaultValue = "9")Integer size){

        PageDTO<Advise> pageDTO=new PageDTO();
        Integer offset=size*(page-1);
        List<Advise> adviseList=adviseMapper.listAll(offset,size);
        Integer totalCount=adviseMapper.getAllCount();
        pageDTO.setPageDTO(totalCount,page,size);
        pageDTO.setDataDTOS(adviseList);


        model.addAttribute("pageDTO",pageDTO);
        return "checkAdvise";
    }

    @GetMapping("/checkAdvise/{adviseId}")
    public String singleAdvise(Model model,
                              @RequestParam(name="page",defaultValue = "1")Integer page,
                              @RequestParam(name="size",defaultValue = "9")Integer size,
                               @PathVariable(name = "adviseId")Long adviseId){

        Advise advise=adviseMapper.findById(adviseId);

        model.addAttribute("title",advise.getTitle());
        model.addAttribute("description",advise.getDescription());
        model.addAttribute("advise_type",advise.getAdviseType());
        model.addAttribute("action","check");
        return "advise";
    }


}
