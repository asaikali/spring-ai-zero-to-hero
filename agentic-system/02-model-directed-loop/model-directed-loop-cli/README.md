# agent-cli

This is the interactive CLI for exploring agentic systems using Spring Shell. It's part of a progressive hands-on series on building intelligent agents with inner monologue, tool use, memory, and more.

## ⚠️ IntelliJ Users: Important Note About Terminal UI

If you run this project from inside IntelliJ (using the green "Run" button), you may see this warning:

```
WARN org.jline: Unable to create a system terminal, creating a dumb terminal
```

This is expected. **IntelliJ's Run Console is not a full-featured terminal.** It lacks support for:

* Cursor positioning
* Key navigation
* Menu selections
* Terminal-based UI components

As a result, **interactive features like agent pickers, progress bars, and selector UIs will not work correctly** inside the IntelliJ console.

---

## ✅ To Use Full Terminal UI

Run the CLI from your system terminal instead:

### From the root of the Git repo

```bash
./mvnw -pl applications/agent-cli spring-boot:run
```

You should now see a proper interactive prompt:

```
agent>
```

---

## 🧪 Try It

```bash
agent create --id morpheus
agent send --text "What is the Matrix?"
agent log
```

Later examples like `agent pick` will bring up an interactive menu **only if you're using a proper terminal**.

---

## 💡 Tip

On macOS or Linux, use **Terminal**, **iTerm**, or any full-featured shell.

On Windows, use **Windows Terminal** or **PowerShell**, not the IntelliJ console.
