package com.fintect.Banking_Engine.modules.invest;

import java.math.BigDecimal;
import java.util.List;

public record RebalanceRequestDto(String portfolioId, List<AssetDto> targetAllocation) {}