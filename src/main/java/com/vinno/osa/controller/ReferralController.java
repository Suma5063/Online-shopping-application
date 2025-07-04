package com.vinno.osa.controller;

import com.vinno.osa.entity.Referral;
import com.vinno.osa.service.ReferralService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/referrals")
public class ReferralController {
    @Autowired
    private ReferralService referralService;

    @PostMapping("/create")
    public ResponseEntity<Referral> createReferral(@RequestBody Referral referral) {
        Referral createdReferral = referralService.createReferral(referral);
        return ResponseEntity.ok(createdReferral);
    }

    @PostMapping("/claim-bonus/{referralCode}")
    public ResponseEntity<String> claimBonus(@PathVariable String referralCode) {
        referralService.claimBonus(referralCode);
        return ResponseEntity.ok("Bonus claimed successfully.");
    }
}
