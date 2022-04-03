package com.bithumb.assetbatch.repository.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;


@AllArgsConstructor
@Getter
public enum SendFlag {
    OPENED("N", "대기"),
    INPROGRESS("R", "진행중"),
    FAILED("F", "실패"),
    DONE("Y", "완료"),
    UNKNOWN("0", "알수없는 코드");

    private String legacyCode;
    private String description;

    public static SendFlag ofLegacyCode(String legacyCode) {
        return Arrays.stream(SendFlag.values())
                .filter(v -> v.getLegacyCode().equals(legacyCode))
                .findAny()
                .orElse(UNKNOWN);
    }
}
