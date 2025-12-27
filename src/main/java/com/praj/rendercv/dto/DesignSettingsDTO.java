package com.praj.rendercv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DesignSettingsDTO {
    private String theme = "modern";
    private PageSettings page = new PageSettings();
    private ColorSettings colors = new ColorSettings();
    private TypographySettings typography = new TypographySettings();

    @JsonProperty("layout")
    private LayoutDTO layout = new LayoutDTO();

    @Data
    public static class PageSettings {
        @JsonProperty("size")
        private String size = "us-letter";
        @JsonProperty("top_margin")
        private String topMargin = "0.75in";
        @JsonProperty("bottom_margin")
        private String bottomMargin = "0.75in";
        @JsonProperty("left_margin")
        private String leftMargin = "0.75in";
        @JsonProperty("right_margin")
        private String rightMargin = "0.75in";
    }

    @Data
    public static class ColorSettings {
        private String body = "#333333";
        private String name = "#000000";

        @JsonProperty("section_titles")
        private String sectionTitles = "#0070e0";

        // NEW: Field for Entry Titles (e.g., Job Positions, Project Names)
        private String titles = "#111111";

        // NEW: Field for Subtitles (e.g., Company Names, Institutions)
        private String subtitles = "#444444";

        @JsonProperty("sidebar_background")
        private String sidebarBackground = "#F8FAFC";

        private String links = "#0070e0";
    }

    @Data
    public static class TypographySettings {
        @JsonProperty("line_spacing")
        private String lineSpacing = "1.4";
        private String alignment = "left";
        @JsonProperty("font_family")
        private String fontFamily = "Source Sans 3";
    }
}