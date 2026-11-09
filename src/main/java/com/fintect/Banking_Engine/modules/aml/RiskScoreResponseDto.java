package com.fintect.Banking_Engine.modules.aml;

import java.util.UUID;

public record RiskScoreResponseDto(UUID accountId, double centralityScore, String riskLevel) {}