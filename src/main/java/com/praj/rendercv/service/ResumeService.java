package com.praj.rendercv.service;

import com.praj.rendercv.dto.ResumeRequest;
import java.io.InputStream;

public interface ResumeService {
    /**
     * Parses raw YAML content into a ResumeRequest DTO.
     */
    ResumeRequest parseYaml(String yamlContent);

    /**
     * Orchestrates the full rendering process.
     * Returns a byte array (the PDF content).
     */
    byte[] generateResumePdf(ResumeRequest request);
}
