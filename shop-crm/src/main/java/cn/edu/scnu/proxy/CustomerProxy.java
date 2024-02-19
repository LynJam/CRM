package cn.edu.scnu.proxy;

import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-crm")
public interface CustomerProxy {
    @GetMapping("/proxy/getUpCustomerIdList")
    List<String> getUpCustomerIdList(@RequestParam("userId") String userId);
}
