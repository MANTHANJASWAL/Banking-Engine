package com.fintect.Banking_Engine.modules.invest;

import java.util.List;


public record OptimizationRequestDto(List<AssetDto> assets, int maxRiskCapacity) {}
