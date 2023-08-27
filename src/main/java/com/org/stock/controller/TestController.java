package com.org.stock.controller;

import com.org.stock.service.AnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app")
public class TestController {
    @Autowired
    private AnalysisService analysisService;

    @GetMapping("/test")
    public String add() {
        analysisService.analysisEtfContract(1L);
        return "ok";
    }
}
