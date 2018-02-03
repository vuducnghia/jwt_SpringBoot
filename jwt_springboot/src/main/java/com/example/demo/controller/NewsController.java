package com.example.demo.controller;

import com.example.demo.Utils.FakeUtils;
import com.example.demo.models.News;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class NewsController {

    @CrossOrigin(origins = "http://localhost:4200")

    @RequestMapping(method = RequestMethod.GET, value = "/api/admin/news")
    public List<News> news(HttpServletRequest request) {
        System.out.println("vao dc 11111");
        System.out.println(FakeUtils.getAllNews());
        return FakeUtils.getAllNews();
    }
}
