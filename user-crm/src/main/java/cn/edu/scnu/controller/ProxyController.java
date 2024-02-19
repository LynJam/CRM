package cn.edu.scnu.controller;

import cn.edu.scnu.service.CustomerRelationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/proxy")
@RestController
public class ProxyController {
    @Autowired
    private CustomerRelationService customerRelationService;

    @GetMapping("/getUpCustomerIdList")
    public List<String> getUpCustomerIdList(@RequestParam("userId") String userId) {
        return customerRelationService.getUpCustomerIdList(userId);
    }
}
