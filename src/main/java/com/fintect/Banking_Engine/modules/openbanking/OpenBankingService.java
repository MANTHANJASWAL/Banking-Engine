package com.fintect.Banking_Engine.modules.openbanking;

import com.fintect.Banking_Engine.core.dsa.RadixTreeRouterEngine;
import org.springframework.stereotype.Service;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@Service
public class OpenBankingService {

    private final RadixTreeRouterEngine routerEngine;

    public OpenBankingService() {
        this.routerEngine = new RadixTreeRouterEngine();
        // Pre-load common Open Banking routes
        this.routerEngine.insertRoute("/accounts/balances", "internal_balance_service");
        this.routerEngine.insertRoute("/accounts/transactions", "internal_tx_service");
        this.routerEngine.insertRoute("/payments/initiate", "internal_payment_gateway");
    }

    public String grantConsent(ConsentRequestDto request) {
        return "TPA_TOKEN_" + UUID.randomUUID().toString().replace("-", "").toUpperCase();
    }

    public RouteMatchDto dispatchRoute(String path) {
        String target = routerEngine.matchRoute(path);
        return new RouteMatchDto(path, target, target != null);
    }
}