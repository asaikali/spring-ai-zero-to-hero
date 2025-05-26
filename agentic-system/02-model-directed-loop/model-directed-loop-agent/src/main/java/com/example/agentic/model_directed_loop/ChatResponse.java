package com.example.agentic.model_directed_loop;

public record ChatResponse(String message, String innerThoughts, boolean requestReinvocation) {}
