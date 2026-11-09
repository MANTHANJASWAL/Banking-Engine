package com.fintect.Banking_Engine.modules.fx;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record FxTradeExecutionDto(UUID sourceAccountId, String startingCurrency, BigDecimal initialAmount, List<String> routePath) {}