# agentic-system

Welcome to the **Agentic System** — a progressive series of hands-on examples that teach you how to
design and build intelligent, tool-using AI agents powered by modern LLMs.

This project focuses on building up a[MemGPT-style agent](https://arxiv.org/abs/2310.08560) from 
first principles. You'll learn how to combine tool calling, inner monologue, memory, and 
multi-shot control flow to create agents that reason, act, and evolve.

## Learning Path

| Project Folder           | Concept                       | What You’ll Learn                                     |
|--------------------------|-------------------------------|--------------------------------------------------------|
| `01-inner-monologue`     | Thought Before Action         | Introduces inner monologue + tool calling.             |
| `02-model-directed-loop` | Control Flow via Reinvocation | Agent controls loop using reinvocation signals.        |
| `03-knowledge-capture`   | Memory as Knowledge           | Captures and stores distilled knowledge.               |
| `04-structured-memory`   | Scoped Knowledge              | Adds structured, purpose-scoped memory.                |
| `05-agent-runtime`       | Model Autonomy via Reentry    | Connects LLMs to business logic with full agent loop.  |

### Detailed Explanations

#### `01-inner-monologue`

Learn the **inner monologue + tool calling** pattern — a foundational agent design where the model first emits private reasoning before taking action. This establishes a cognitive flow of **perception → reasoning → action**, making each tool call a transparent and explainable decision.

#### `02-model-directed-loop`

The model now has control over whether the agent loop continues. Using the `request_reinvocation` signal, it can think, act, and then choose to run again — or exit. This is the first example where the model drives multi-step behavior, turning inner monologue + tool calls into a full agent loop.

#### `03-knowledge-capture`

Demonstrates **self-editing memory** and **knowledge capture** — where the model interprets user input, distills it into structured memory, and stores it before responding. This shows how agents build long-term understanding over time, laying the foundation for personalization and continuity.

#### `04-structured-memory`

Builds on `knowledge-capture` by introducing **structured memory blocks**. The model doesn't just decide *what* to remember — it must also decide *where*. By targeting the `<human>` memory block, this pattern teaches scoped, purpose-driven memory management and prepares agents for more complex multi-slot reasoning.

#### `05-agent-runtime`

Implements the core **agent runtime loop** and introduces **tool use beyond memory**. The model can now search, plan, query APIs — and your code orchestrates the loop, tool calls, and reentry. This module shows how to safely connect LLMs to business logic in a model-directed flow.

## Final Note

This project isn't just about calling tools — it's about teaching your model to **think**, **act**,
and **iterate**. Each example gives the model just a bit more autonomy — until it becomes something
agentic.
