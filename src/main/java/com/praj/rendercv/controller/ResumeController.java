package com.praj.rendercv.controller;

import com.praj.rendercv.dto.ResumeRequest;
import com.praj.rendercv.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/v1/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;

    @PostMapping("/test-parse")
    public ResumeRequest testParsing(@RequestBody String yamlContent) {
        // This will call the service we built in Module 2
        return resumeService.parseYaml(yamlContent);
    }

    @PostMapping("/render")
    public ResponseEntity<byte[]> renderResume(@RequestBody String yamlContent) {
        // 1. Parse the YAML
        ResumeRequest request = resumeService.parseYaml(yamlContent);

        // 2. Generate PDF bytes
        byte[] pdfBytes = resumeService.generateResumePdf(request);

        // 3. Return as a downloadable/viewable PDF file
        String fileName = request.getCv().getName().replace(" ", "_") + "_CV.pdf";

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfBytes);
    }
}
