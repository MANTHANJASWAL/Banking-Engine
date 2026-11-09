package com.fintect.Banking_Engine.modules.fx;

import java.math.BigDecimal;
import java.util.List;
public record ArbitrageResponseDto(boolean opportunityFound, List<String> executionPath, BigDecimal estimatedProfitMargin) {}
