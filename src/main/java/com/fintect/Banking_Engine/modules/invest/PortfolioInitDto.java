package com.fintect.Banking_Engine.modules.invest;

import java.math.BigDecimal;
import java.util.List;

public record PortfolioInitDto(String portfolioName, BigDecimal initialCapital) {}