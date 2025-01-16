package com.challenge.fullstack.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties(ignoreUnknown = true)
public class CountryDto {

    private Name name;
    private Flags flags;

    // Constructor vacío
    public CountryDto() {
    }

    // Constructor completo
    public CountryDto(Name name, Flags flags) {
        this.name = name;
        this.flags = flags;
    }

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
        private String official;

        // Constructor vacío
        public Name() {
        }

        // Constructor para ambos campos
        public Name(String common, String official) {
            this.common = common;
            this.official = official;
        }

        // Constructor para un solo campo (common)
        public Name(String common) {
            this.common = common;
        }

        public String getCommon() {
            return common;
        }

        public void setCommon(String common) {
            this.common = common;
        }

        public String getOfficial() {
            return official;
        }

        public void setOfficial(String official) {
            this.official = official;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Flags {
        private String png;
        private String svg;

        // Constructor vacío
        public Flags() {
        }

        // Constructor completo
        public Flags(String png, String svg) {
            this.png = png;
            this.svg = svg;
        }

        // Constructor para un solo campo (png)
        public Flags(String png) {
            this.png = png;
        }

        public String getPng() {
            return png;
        }

        public void setPng(String png) {
            this.png = png;
        }

        public String getSvg() {
            return svg;
        }

        public void setSvg(String svg) {
            this.svg = svg;
        }
    }
}
