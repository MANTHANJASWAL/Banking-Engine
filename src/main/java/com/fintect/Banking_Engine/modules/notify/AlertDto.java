package com.fintect.Banking_Engine.modules.notify;

public record AlertDto(String alertId, String severity, String message, long timestamp) {}