package com.vinno.osa.repository;

import com.vinno.osa.entity.Referral;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReferralRepository extends MongoRepository<Referral,String> {
    Referral findByReferralCode(String referralCode);
}
