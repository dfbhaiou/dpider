package cn.dpider.monitor.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * @author wanhongji
 * @date 2019/04/09
 */
@Controller
public class UIController {

    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page){
        return page;
    }

}
