package com.fintect.Banking_Engine.modules.credit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record CreditEvalRequestDto(List<String> rulesToApply, Map<String, List<String>> ruleDependencies) {}
