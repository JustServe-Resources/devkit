
# CLI Command Naming Convention Standard
**Noun–Verb–Flags Pattern**

## Purpose

This document defines the standard approach for designing CLI commands and flags. The goals are to:

- Improve **clarity and discoverability** of commands
- Ensure **consistency** across all commands and resources
- Make the CLI **intuitive**, **scalable**, and **future‑proof**
- Reduce cognitive load when learning or extending the CLI

This standard should be followed for all new CLI commands and when refactoring existing ones.

---

## Core Principles

### Use the Noun–Verb–Flags Structure

All commands must follow this structure:
#### Definitions
- **Noun**: The primary resource being acted upon (e.g., `organization`, `project`, `user`)
- **Verb**: The action being performed on that resource (e.g., `assign`, `create`, `remove`)
- **Flags**: Parameters describing *how* or *to whom* the action applies

#### Why This Works
### - Mirrors natural language (“assign a user to an organization”)
- Scales cleanly as resources and actions grow
- Matches conventions used by mature CLIs (e.g., `kubectl`, `git`)
- Enables easy auto-completion and documentation generation

---

## Example

### ❌ Non‑Standard / Legacy Command
**Issues**
- Verb-first command is unclear
- Action is tightly coupled to a specific use case
- User identification is inconsistent and limited

---

### ✅ Standardized Command
**Benefits**
- Clearly communicates the resource (`organization`)
- Action (`assign`) is consistent across contexts
- Flags are explicit, flexible, and consistent

---

## Flag Consistency Rules

### Flags Must Be Consistent Across the Entire CLI

If a resource or concept appears in multiple commands, it must always use the same flag name and behavior, and accept all existing input variations

#### User References

Any flag referring to a user (e.g., `-u`, `--user`) must consistently support every current reference
method, and if you choose to add a new reference method all other commands that use that reference should be updated.

✅ Example: 
If when creating a reassign org command we decide to reference the user by first and last name,
as well as using the supported method of UUID, or email you must update the other commands to retain
consistency across all commands.

consistency is key every reference to a user must provide every reference option, and every reference to an organization must provide **every** reference option without exception.

---

## Design Guidelines Summary

### ✅ Do
- Use **noun → verb → flags**
- Keep flag names and behavior **globally consistent**
- Ensure related commands support the **same inputs**

### ❌ Don’t
- Create verb-first or one-off command names
- Introduce new flags for existing concepts
- Restrict identifiers in one command but not others
- Optimize for short-term convenience over long-term clarity

---

## Why This Standard Matters

Following this convention results in:

- Fewer breaking changes as the CLI evolves
- A more intuitive and predictable developer experience
- Easier automation and scripting

Consistency is not a cosmetic decision—it is a core usability feature.

---

## Conclusion

The **Noun–Verb–Flags** system, combined with **strict flag consistency and identifier flexibility**, provides a clean, extensible foundation for CLI design. Adhering to this standard ensures that every new command feels familiar, predictable, and easy to use.

All future CLI development should conform to this document unless a well‑justified exception is explicitly approved.
