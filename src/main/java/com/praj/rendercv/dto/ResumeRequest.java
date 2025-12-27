package com.praj.rendercv.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.Map;

@Data
public class ResumeRequest {
    @NotNull(message = "CV content section ('cv:') is missing")
    @Valid // This is crucial to trigger validation of the nested CvContentDTO
    private CvContentDTO cv;

    @Valid
    private DesignSettingsDTO design = new DesignSettingsDTO();

    private Map<String, String> locale;
}