package com.bithumb.assetbatch.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MvpService {

    public String sendMessage(String message) {
        log.info("success semnd message : {}", message);
        return "0000";
    }
}
