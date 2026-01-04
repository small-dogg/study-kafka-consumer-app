package com.smalldogg.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CashStore {

    private final GroupLimitRepository groupLimitRepository;
    private final PartnerCashRepository partnerCashRepository;
    private final PartnerLimitRepository  partnerLimitRepository;

    public void deductCash() {
        //캐시 차감
        //예산 차감


    }
}
