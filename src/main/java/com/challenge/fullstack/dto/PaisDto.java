package com.challenge.fullstack.dto;

import lombok.Data;

@Data
public class PaisDto {
    private Name name;
    private Flags flags;

    @Data
    public static class Name {
        private String common;
    }

    @Data
    public static class Flags {
        private String png;
    }
}
