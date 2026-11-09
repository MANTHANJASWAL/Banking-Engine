package com.fintect.Banking_Engine.modules.aml;

import java.util.List;
import java.util.UUID;

public record CycleResponseDto(int totalCyclesDetected, List<List<UUID>> cyclePaths) {}