package com.praj.rendercv.dto;

import lombok.Data;
import java.util.List;
import java.util.ArrayList;

@Data
@com.fasterxml.jackson.annotation.JsonIgnoreProperties(ignoreUnknown = true)
public class LayoutDTO {
    @com.fasterxml.jackson.annotation.JsonProperty("main")
    private java.util.List<String> main = new java.util.ArrayList<>();

    @com.fasterxml.jackson.annotation.JsonProperty("sidebar")
    private java.util.List<String> sidebar = new java.util.ArrayList<>();
}