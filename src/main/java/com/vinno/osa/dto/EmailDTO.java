package com.vinno.osa.dto;

import lombok.Data;

@Data
public class EmailDTO {

    private String recipientEmail;

    private String subject;

    private String messageBody;

    // Default constructor
    public EmailDTO() {

    }

    // Constructor with parameters
    public EmailDTO(String recipientEmail, String subject, String messageBody)
    {
        this.recipientEmail = recipientEmail;
        this.subject = subject;
        this.messageBody = messageBody;
    }

}
