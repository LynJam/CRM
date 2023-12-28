package cn.edu.scnu.controller;

import cn.edu.scnu.dto.RespRes;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/customer")
@RestController
public class CustomerController {
    @PostMapping("/add")
    public RespRes addCustomer() {
        return RespRes.success("ok");
    }
}
