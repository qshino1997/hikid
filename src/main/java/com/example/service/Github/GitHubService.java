package com.example.service.Github;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


@Service
public class GitHubService {
    private String token, owner, repo, branch, baseApi;

    @Autowired
    private RestTemplate rest;

    @Autowired
    private Environment env;

    @PostConstruct
    public void init() {
        token   = env.getProperty("GITHUB_TOKEN");
        owner   = env.getProperty("GITHUB_REPO_OWNER");
        repo    = env.getProperty("GITHUB_REPO_NAME");
        branch  = env.getProperty("GITHUB_BRANCH", "main");
        baseApi = env.getProperty("GITHUB_BASE_API", "https://api.github.com");

        // ----- VALIDATION -----
        if (token == null || token.isBlank()) {
            throw new IllegalStateException("Environment variable GITHUB_TOKEN is not set or empty");
        }
        if (owner == null || owner.isBlank()) {
            throw new IllegalStateException("Environment variable GITHUB_REPO_OWNER is not set or empty");
        }
        if (repo == null || repo.isBlank()) {
            throw new IllegalStateException("Environment variable GITHUB_REPO_NAME is not set or empty");
        }

        System.out.println("GitHubService initialized with repo " + owner + "/" + repo + "@" + branch);

    }

    /**
     * Đẩy file lên GitHub, trả về raw URL.
     */
    public String uploadFile(String pathInRepo, byte[] contentBytes, String commitMessage) {
        String url = String.format("%s/repos/%s/%s/contents/%s",
                baseApi, owner, repo, pathInRepo);
        String contentBase64 = Base64.getEncoder().encodeToString(contentBytes);

        Map<String,Object> body = new HashMap<>();
        body.put("message", commitMessage);
        body.put("content", contentBase64);
        body.put("branch", branch);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String,Object>> req = new HttpEntity<>(body, headers);

        try {
            ParameterizedTypeReference<Map<String, Object>> typeRef =
                    new ParameterizedTypeReference<Map<String, Object>>() { };
            ResponseEntity<Map<String, Object>> response = rest.exchange(url, HttpMethod.PUT, req, typeRef);

            validateResponse(response);
            return extractDownloadUrl(response.getBody());

        } catch (HttpClientErrorException e) {
            System.out.println("GitHub API error: " + e.getStatusCode() + "- " + e.getResponseBodyAsString());
            throw new IllegalStateException("Failed to upload file to GitHub: " + e.getMessage(), e);
        }
    }

    /**
     * Validate HTTP status and presence of content key.
     */
    private void validateResponse(ResponseEntity<Map<String, Object>> response) {
        HttpStatus status = response.getStatusCode();
        if (!(status == HttpStatus.CREATED || status == HttpStatus.OK)) {
            throw new IllegalStateException("GitHub API returned unexpected status: " + status);
        }
        Map<String, Object> respBody = response.getBody();
        if (respBody == null || !respBody.containsKey("content")) {
            throw new IllegalStateException("Empty or invalid response from GitHub API");
        }
    }

    /**
     * Extract download_url from API response body.
     */
    private String extractDownloadUrl(Map<String, Object> respBody) {
        // Ensure respBody is not null to satisfy static analysis
        Objects.requireNonNull(respBody, "Response body must not be null");

        @SuppressWarnings("unchecked")
        Map<String, Object> content = (Map<String, Object>) respBody.get("content");
        Objects.requireNonNull(content, "Content section is missing in response body");

        Object downloadUrl = content.get("download_url");
        if (downloadUrl == null) {
            throw new IllegalStateException("download_url not found in GitHub API response");
        }
        return downloadUrl.toString();
    }
}
