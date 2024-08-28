package com.zhaozhong.controller;


import com.zhaozhong.model.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
public class UserController {


    @GetMapping("/name")
    public String getNameFromGateway(@RequestParam String name,HttpServletRequest request){
        return "your name:"+name;
    }

    @PostMapping("/post/api")
    public String getApiServiceByPost(@RequestBody User user, HttpServletRequest request){

        String body = request.getHeader("body");
        String clientSign = request.getHeader("sign");
        return "您好！"+user.getUsername()+",您的API调用成功！";

    }
    
}
