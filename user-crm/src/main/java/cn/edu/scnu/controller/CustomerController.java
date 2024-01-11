package cn.edu.scnu.controller;

import cn.edu.scnu.dto.RespRes;
import cn.edu.scnu.entity.AddUpCustomerApplicationEntity;
import cn.edu.scnu.entity.CustomerRelationEntity;
import cn.edu.scnu.entity.UserEntity;
import cn.edu.scnu.service.AddUpCustomerApplicationService;
import cn.edu.scnu.service.CustomerRelationService;
import cn.edu.scnu.service.UserService;
import cn.edu.scnu.vo.CustomerVo;
import cn.hutool.core.bean.BeanUtil;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/customer")
@RestController
public class CustomerController {
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerRelationService customerRelationService;
    @Autowired
    private AddUpCustomerApplicationService addUpCustomerApplicationService;

    @PostMapping("/allowAdd")
    public RespRes allowAdd(@RequestBody AddUpCustomerApplicationEntity arg) {
        addUpCustomerApplicationService.remove(arg.getApplicantId(), arg.getUpUserId());
        customerRelationService.save(arg.getApplicantId(), arg.getUpUserId());
        // TODO: 2021/4/25 添加客户关系后，需要通知申请者, 建立房间
        return RespRes.success("ok");
    }

    @PostMapping("/delCustomer")
    public RespRes delCustomer(@RequestBody CustomerRelationEntity arg) {
        customerRelationService.remove(arg.getDownUserId(), arg.getUpUserId());
        return RespRes.success("ok");
    }

    @PostMapping("/searchCustomer/{upUserId}")
    public RespRes searchCustomer(@PathVariable("upUserId") String upUserId, @RequestHeader("userId") String userId) {
        UserEntity upUser = userService.getById(upUserId);
        if (ObjectUtils.isNotEmpty(upUser)) {
            Boolean added = customerRelationService.havenAddUpUser(userId, upUserId);
            upUser.setPassword(null);
            CustomerVo customerVo = new CustomerVo();
            BeanUtil.copyProperties(upUser, customerVo);
            customerVo.setAdded(added);
            return RespRes.success(customerVo);
        } else {
            return RespRes.success(null);
        }
    }

    @PostMapping("/applyToAdd")
    public RespRes applyToAdd(@RequestBody AddUpCustomerApplicationEntity arg) {
        addUpCustomerApplicationService.save(arg);
        return RespRes.success("ok");
    }

    @PostMapping("/getApplicationList")
    public RespRes getApplicationList(@RequestHeader("userId") String userId) {
        List<String> applicantIds = addUpCustomerApplicationService.findApplicantIds(userId);
        List<CustomerVo> customers = userService.findUsersByIds(applicantIds)
            .stream()
            .map(user -> {
                CustomerVo customerVo = new CustomerVo();
                BeanUtil.copyProperties(user, customerVo);
                return customerVo;
            })
            .collect(Collectors.toList());
        return RespRes.success(customers);
    }

    @PostMapping("/refuseAdd")
    public RespRes refuseAdd(@RequestBody AddUpCustomerApplicationEntity arg) {
        addUpCustomerApplicationService.remove(arg.getApplicantId(), arg.getUpUserId());
        return RespRes.success("ok");
    }

    @GetMapping("/getCustomerList")
    public RespRes getCustomerList(@RequestHeader("userId") String userId) {
        List<String> downCustomerIds = customerRelationService.findDownCustomerIds(userId);
        List<CustomerVo> customerVos = userService.findUsersByIds(downCustomerIds)
            .stream()
            .map(user -> {
                CustomerVo customerVo = new CustomerVo();
                BeanUtil.copyProperties(user, customerVo);
                customerVo.setUpUser(false);
                return customerVo;
            })
            .collect(Collectors.toList());
        List<String> upCustomerIds = customerRelationService.findUpCustomerIds(userId);
        List<CustomerVo> upCustomers = userService.findUsersByIds(upCustomerIds)
            .stream()
            .map(user -> {
                CustomerVo customerVo = new CustomerVo();
                BeanUtil.copyProperties(user, customerVo);
                customerVo.setUpUser(true);
                return customerVo;
            })
            .collect(Collectors.toList());
        customerVos.addAll(upCustomers);
        return RespRes.success(customerVos);
    }
}
