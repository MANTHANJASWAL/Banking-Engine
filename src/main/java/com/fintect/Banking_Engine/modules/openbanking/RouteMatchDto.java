package com.fintect.Banking_Engine.modules.openbanking;


public record RouteMatchDto(String requestedPath, String resolvedTarget, boolean matched) {}