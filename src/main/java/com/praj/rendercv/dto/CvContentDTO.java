package com.praj.rendercv.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class CvContentDTO {
    @NotBlank(message = "Name is required")
    private String name;

    private String photoUrl; // New field for the image URL or Base64

    private String location;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    private String website;

    @JsonProperty("social_networks")
    private List<SocialNetwork> socialNetworks;

    private Map<String, Object> sections;

    @Data
    public static class SocialNetwork {
        private String network;
        private String username;
    }
}