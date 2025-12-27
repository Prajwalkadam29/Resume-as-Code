package com.praj.rendercv.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import com.praj.rendercv.dto.ApiError;
import com.praj.rendercv.dto.ResumeRequest;
import com.praj.rendercv.exception.YamlParsingException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class ResumeServiceImpl implements ResumeService {

    private final YAMLMapper yamlMapper;
    private final SpringTemplateEngine templateEngine;
    private final Validator validator;
    private final ResourceService resourceService; // Inject the new service

    @Override
    public ResumeRequest parseYaml(String yamlContent) {
        try {
            ResumeRequest request = yamlMapper.readValue(yamlContent, ResumeRequest.class);
            validateRequest(request);
            return request;
        } catch (JsonProcessingException e) {
            throw new YamlParsingException("Invalid YAML syntax: " + e.getOriginalMessage(), e);
        }
    }

    @Override
    public byte[] generateResumePdf(ResumeRequest request) {
        try {
            // 1. Ensure Design Settings are never null (Safety Net)
            if (request.getDesign() == null) {
                request.setDesign(new com.praj.rendercv.dto.DesignSettingsDTO());
            }

            // 2. Fallback for Layout: If user didn't specify, use all section keys
            if (request.getDesign().getLayout() == null ||
                    (request.getDesign().getLayout().getMain().isEmpty() &&
                            request.getDesign().getLayout().getSidebar().isEmpty())) {

                log.info("No layout specified, applying default section mapping.");
                request.getDesign().getLayout().setMain(
                        new java.util.ArrayList<>(request.getCv().getSections().keySet())
                );
            }

            // 3. Process Photo
            String photoUrl = request.getCv().getPhotoUrl();
            if (photoUrl != null && photoUrl.startsWith("http")) {
                request.getCv().setPhotoUrl(resourceService.imageUrlToBase64(photoUrl));
            }

            // 4. Sanitize Data (Recursive & amp; fix)
            sanitizeRequest(request);

            Context context = new Context();
            context.setVariable("resume", request);
            context.setVariable("selectedFont", request.getDesign().getTypography().getFontFamily());

            String processedHtml = templateEngine.process("themes/" + request.getDesign().getTheme(), context);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();

                addFontFromCache(builder, "fonts/Source-Sans-3/SourceSans3-Regular.ttf", "Source Sans 3", 400);
                addFontFromCache(builder, "fonts/Source-Sans-3/SourceSans3-Bold.ttf", "Source Sans 3", 700);

                // Add these two lines to support Roboto:
                addFontFromCache(builder, "fonts/Roboto/Roboto-Regular.ttf", "Roboto", 400);
                addFontFromCache(builder, "fonts/Roboto/Roboto-Bold.ttf", "Roboto", 700);

                builder.withHtmlContent(processedHtml, "/");
                builder.toStream(outputStream);
                builder.run();
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            log.error("PDF Generation Critical Error", e);
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private void addFontFromCache(PdfRendererBuilder builder, String path, String family, int weight) {
        byte[] fontData = resourceService.getFontBytes(path);
        if (fontData != null) {
            builder.useFont(() -> new ByteArrayInputStream(fontData), family, weight, BaseRendererBuilder.FontStyle.NORMAL, true);
        }
    }

    private void sanitizeRequest(ResumeRequest request) {
        if (request.getCv().getSections() != null) {
            request.getCv().getSections().replaceAll((k, v) -> sanitizeRecursive(v));
        }
    }

    private Object sanitizeRecursive(Object value) {
        if (value instanceof String) {
            return ((String) value).replace("&", "&amp;");
        } else if (value instanceof List) {
            return ((List<?>) value).stream().map(this::sanitizeRecursive).toList();
        } else if (value instanceof java.util.Map) {
            java.util.Map<String, Object> map = (java.util.Map<String, Object>) value;
            map.replaceAll((k, v) -> sanitizeRecursive(v));
            return map;
        }
        return value;
    }

    private void validateRequest(ResumeRequest request) {
        Set<ConstraintViolation<ResumeRequest>> violations = validator.validate(request);
        if (!violations.isEmpty()) {
            List<ApiError.ValidationError> details = violations.stream()
                    .map(v -> ApiError.ValidationError.builder()
                            .field(v.getPropertyPath().toString())
                            .message(v.getMessage())
                            .build())
                    .toList();
            throw new YamlParsingException("The provided resume data is invalid", details);
        }
    }
}