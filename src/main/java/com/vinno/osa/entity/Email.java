package com.vinno.osa.entity;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

public class Email {

    @Id
    private String id;

    private String userId;

    private String subject;
    private String content;
    private String type; // e.g., "Transactional", "Promotional"

    private LocalDateTime sentAt;

    public Email(String id, String userId, String subject, String content, String type, LocalDateTime sentAt) {
        this.id = id;
        this.userId = userId;
        this.subject = subject;
        this.content = content;
        this.type = type;
        this.sentAt = sentAt;
    }

    public Email() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }
}
