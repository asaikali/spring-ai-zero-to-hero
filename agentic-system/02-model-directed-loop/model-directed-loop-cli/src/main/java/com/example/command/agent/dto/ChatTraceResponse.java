package com.example.command.agent.dto;

import java.util.List;

public record ChatTraceResponse(List<ChatResponse> steps) {}
