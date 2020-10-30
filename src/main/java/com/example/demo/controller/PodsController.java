/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Test
 */
@RestController
public class PodsController {

    @Autowired
    private GenerateName generateName;

    @GetMapping("/")
    public HashMap<String, String> getPodName() {
        return generateName.getPodDetail();
    }

    @GetMapping("/openshift")
    public HashMap<String, String> openshift() {
        HashMap<String, String> message = new HashMap();
        message.put("Che", "Openshift");
        return message;
    }
    
    @GetMapping("/test")
    public String test() {
        String a = "[FIX] ลบอักขระพิเศษ / \\ \" ' ( ) ออกไปป้องกัน sed มีปัญหา escape sequence";
        return a.replaceAll("(\')|(\")|(/)|(\\\\)|(\\()|(\\))", "");
    }
    
    @GetMapping("/image")
    public String image() {
        String a = "devopsculture.azurecr.io/dev/spring-random-pod:v1.0.0";
        return a.replaceAll("/","\\\\/");
    }
}
