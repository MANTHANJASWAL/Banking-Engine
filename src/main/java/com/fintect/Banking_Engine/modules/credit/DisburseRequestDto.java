package com.fintect.Banking_Engine.modules.credit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public record DisburseRequestDto(UUID accountId, BigDecimal loanAmount) {}