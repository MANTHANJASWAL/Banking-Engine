package com.fintect.Banking_Engine.modules.openbanking;

public record ConsentRequestDto(String clientId, String scope, String accountId) {}