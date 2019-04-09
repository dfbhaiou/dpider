package cn.dpider.monitor.controller;

import cn.dpider.monitor.po.Page;
import cn.dpider.monitor.service.MonitorService;
import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author wanhongji
 * @date 2019/04/09
 */
@Controller
@RequestMapping(value = "/monitor",produces = {"application/json;charset=UTF-8"} )
public class MonitorController {

    @Autowired
    MonitorService monitorService;

    @RequestMapping("/allSpider")
    @ResponseBody
    public String allSpiders(Page page){
        return JSON.toJSONString(monitorService.getAllSpider(page));
    }

    @RequestMapping("/allUrlScheduler")
    @ResponseBody
    public String allUrlScheduler(Page page){
        return JSON.toJSONString(monitorService.getAllUrlScheduler(page));
    }
}
