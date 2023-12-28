package cn.edu.scnu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MongoController {
    @Autowired
    private MongoService mongoService;

    @RequestMapping("test")
    public String find() {
        return mongoService.findAll();
    }
}
