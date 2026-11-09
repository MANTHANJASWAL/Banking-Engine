package com.fintect.Banking_Engine.modules.invest;

import java.math.BigDecimal;
import java.util.List;


public record AssetDto(String assetId, int riskScore, double expectedYield) {}
