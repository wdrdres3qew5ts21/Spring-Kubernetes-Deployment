/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.example.demo.controller;

import com.github.javafaker.Faker;
import java.util.Date;
import java.util.HashMap;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 *
 * @author Test
 */
@Service
public class GenerateName {

    @Value("${env.server_environment:LOCAL_MACHINE_STAGE}")
    private String STAGE;

    @Value("${env.branch:MOCK_BRANCH}")
    private String BRANCH;

    @Value("${env.version:v0.0.0}")
    private String VERSION;

    private HashMap<String, String> podDetail;

    private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

    public static String getClientIpAddressIfServletRequestExist() {

        if (RequestContextHolder.getRequestAttributes() == null) {
            return "0.0.0.0";
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ip;
            }
        }
        return request.getRemoteAddr();
    }

    @PostConstruct
    public void generatePod() {
        Faker faker = new Faker();
        HashMap<String, String> detail = new HashMap();
        String name = faker.name().fullName();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String streetAddress = faker.address().streetAddress();
        detail.put("test_update", "Im on Dev Branch !");
        detail.put("stage", STAGE);
        detail.put("version", VERSION);
        detail.put("branch", BRANCH);
        detail.put("name", name);
        detail.put("firstName", firstName);
        detail.put("lastName", lastName);
        detail.put("streetAddress", streetAddress);
        detail.put("time", new Date().toString());
        detail.put("clientIp", getClientIpAddressIfServletRequestExist());
        this.podDetail = detail;
        System.out.println(this.podDetail);
    }
    
    public HashMap<String, String> getPodDetail() {
        return podDetail;
    }

}
