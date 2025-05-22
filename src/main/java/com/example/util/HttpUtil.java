package com.example.util;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Utility class to send HTTP requests.
 */
public class HttpUtil {

    /**
     * Send a POST request with JSON payload and Bearer authorization.
     *
     * @param endpointUrl the full URL of the endpoint
     * @param accessToken a valid OAuth 2.0 bearer token
     * @param jsonPayload the JSON body to send
     * @return the response body as String
     * @throws IOException in case of network or stream errors
     */
    public static String postJson(String endpointUrl, String accessToken, String jsonPayload) throws IOException {
        HttpURLConnection conn = null;
        try {
            URL url = new URL(endpointUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Set headers
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "Bearer " + accessToken);

            // Write JSON payload
            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = jsonPayload.getBytes("UTF-8");
                os.write(input);
                os.flush();
            }

            // Read response
            int status = conn.getResponseCode();
            InputStream is = (status >= HttpURLConnection.HTTP_BAD_REQUEST)
                    ? conn.getErrorStream()
                    : conn.getInputStream();

            StringBuilder response = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
            }

            return response.toString();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        String line;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        return sb.toString();
    }
}

