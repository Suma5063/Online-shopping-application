package com.vinno.osa.service;

import com.vinno.osa.entity.Referral;
import com.vinno.osa.repository.ReferralRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReferralService {
    @Autowired
    private ReferralRepository referralRepository;

    // Method to create a referral record
    public Referral createReferral(Referral referral) {
        return (Referral) referralRepository.save(referral);
    }

    // Method to claim bonus
    public void claimBonus(String referralCode) {
        Referral referral = referralRepository.findByReferralCode(referralCode);
        if (referral != null && !referral.isBonusClaimed()) {
            // Add bonus logic here
            referral.setBonusClaimed(true);
            referralRepository.save(referral);
        }
    }
}
