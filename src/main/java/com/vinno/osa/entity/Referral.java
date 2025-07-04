package com.vinno.osa.entity;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "Referrals")
public class Referral {

    private String id;
    private String userId;
    private String referralCode;
    private String referredUserId;
    private boolean bonusClaimed;

}
