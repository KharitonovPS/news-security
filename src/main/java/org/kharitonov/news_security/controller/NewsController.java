package org.kharitonov.news_security.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.kharitonov.news_security.security.PersonDetails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author Kharitonov Pavel on 30.01.2024.
 */
@RestController()
@Slf4j
public class NewsController {

    @GetMapping("/api/v1/greetings")
    public ResponseEntity<Map<String, String>> getGreetings() {
        PersonDetails userDetails =(PersonDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("greeting", "Hello, %s!"
                        .formatted(userDetails.getUsername())));
    }

    @GetMapping("/api/v2/greetings")
    public ResponseEntity<Map<String, String>> getGreetingsV2(HttpServletRequest request) {
        PersonDetails userDetails =(PersonDetails) ((Authentication) request.getUserPrincipal())
                .getPrincipal();
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("greeting", "Hello, %s!"
                        .formatted(userDetails.getUsername())));
    }

    @GetMapping("/api/v3/greetings")
    public ResponseEntity<Map<String, String>> getGreetingsV3(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        log.info("Request fas taken from principal -> {}", userDetails.getUsername());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of("greeting", "Hello, %s!"
                        .formatted(userDetails.getUsername())));
    }

}
