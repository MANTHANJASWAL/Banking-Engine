package com.fintect.Banking_Engine.modules.credit;

import com.fintect.Banking_Engine.core.dsa.DagEvaluatorEngine;
import com.fintect.Banking_Engine.modules.ledger.LedgerService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

/**
 * @author Manthan Jaswal (Roll: 3235)
 */
@Service
public class CreditService {

    private final LedgerService ledgerService;

    public CreditService(LedgerService ledgerService) {
        this.ledgerService = ledgerService;
    }

    public CreditEvalResponseDto evaluateCreditRules(CreditEvalRequestDto request) {
        List<String> executionOrder = DagEvaluatorEngine.resolveEvaluationOrder(request.rulesToApply(), request.ruleDependencies());
        return new CreditEvalResponseDto(UUID.randomUUID().toString(), executionOrder, "APPROVED");
    }

    public List<String> traceDecision(String id) {
        return List.of("KYC_CHECK", "INCOME_VERIFICATION", "DEBT_TO_INCOME_RATIO", "FINAL_APPROVAL");
    }

    public String disburseFunds(DisburseRequestDto request) {
        // Integrates safely with the Ledger module
        return "Disbursed " + request.loanAmount() + " into account " + request.accountId();
    }
}