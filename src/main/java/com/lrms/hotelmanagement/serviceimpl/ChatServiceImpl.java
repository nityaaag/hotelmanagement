package com.lrms.hotelmanagement.serviceimpl;

import com.lrms.hotelmanagement.service.ChatService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ChatServiceImpl implements ChatService {

    @Value("${groq.api.key}")
    private String apiKey;

    private final String GROQ_URL = "https://api.groq.com/openai/v1/chat/completions";
    private final RestTemplate restTemplate;

    public ChatServiceImpl() {
        this.restTemplate = new RestTemplate();
    }

    @Override
    public String getAiResponse(String message) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return "Error: Groq API Key is missing. Please check your application.properties.";
        }

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            // Ensure the Bearer prefix is present
            headers.set("Authorization", "Bearer " + apiKey);

            // Build Payload
            JSONObject root = new JSONObject();
            root.put("model", "llama-3.1-8b-8192");
            
            JSONArray messages = new JSONArray();
            JSONObject msgObj = new JSONObject();
            msgObj.put("role", "user");
            msgObj.put("content", message);
            messages.put(msgObj);
            
            root.put("messages", messages);

            HttpEntity<String> entity = new HttpEntity<>(root.toString(), headers);

            ResponseEntity<String> response = restTemplate.exchange(
                GROQ_URL,
                HttpMethod.POST,
                entity,
                String.class
            );

            if (response.getStatusCode() == HttpStatus.OK) {
                JSONObject responseJson = new JSONObject(response.getBody());
                return responseJson.getJSONArray("choices")
                        .getJSONObject(0)
                        .getJSONObject("message")
                        .getString("content");
            } else {
                return "Error from AI Service: " + response.getStatusCode();
            }

        } catch (Exception e) {
            return "AI Assistant is currently unavailable: " + e.getMessage();
        }
    }
}