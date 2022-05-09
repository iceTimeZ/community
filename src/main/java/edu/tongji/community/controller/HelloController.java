package edu.tongji.community.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/haha")
public class HelloController {

    @RequestMapping("/hello")
    public String hello(@RequestParam(value="id",required = false,defaultValue = "周才波") String name){
        System.out.println("我的环境搭建好了"+ name + "你好");
        return "这是我的第一个示例";
    }
}
