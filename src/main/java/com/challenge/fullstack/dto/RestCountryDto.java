package com.challenge.fullstack.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RestCountryDto {
    private Name name;
    private Flags flags;

    // Getters y setters
    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Flags getFlags() {
        return flags;
    }

    public void setFlags(Flags flags) {
        this.flags = flags;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Name {
        private String common;

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Flags {
        private String png;

        public String getPng() {
            return png;
        }

        public void setPng(String png) {
            this.png = png;
        }
    }
}
