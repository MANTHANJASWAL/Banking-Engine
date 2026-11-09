package com.fintect.Banking_Engine.modules.invest;

import java.util.List;


public record OptimizationResponseDto(List<AssetDto> selectedAssets, double totalProjectedYield) {}