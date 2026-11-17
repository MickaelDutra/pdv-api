# Domain Documentation Configuration

## Layout: Single-Context

This repository uses a **single-context** domain documentation layout:

```
repo-root/
├── GLOSSARY.md          # Canonical domain terms & definitions
└── docs/
    └── adr/             # Architecture Decision Records
        ├── 0001-example-decision.md
        └── ...
```

## File Purposes

### `GLOSSARY.md`
- **Single source of truth** for domain terminology
- Each term has: definition, aliases, related terms, and ownership
- Written for both humans and agents to consume
- Updated when new domain concepts emerge or existing ones shift

### `docs/adr/`
- **Architecture Decision Records** — one file per significant decision
- Naming: `NNNN-short-kebab-case-title.md` (zero-padded sequence)
- Statuses: `Proposed`, `Accepted`, `Superseded`, `Deprecated`
- Each ADR links to related glossary terms and other ADRs

## Consumer Rules

**For agents reading domain docs:**
1. **Always read `GLOSSARY.md` first** — it defines the vocabulary used in specs, issues, and code
2. **Read relevant ADRs** when working on architecture-adjacent changes
3. **Do not assume** terms not in the glossary; ask for clarification
4. **Update the glossary** when you introduce or change a domain term in a spec or implementation

**For humans maintaining domain docs:**
1. Keep definitions **concise and operational** — not encyclopedic
2. Include **aliases** so searches catch variant spellings/abbreviations
3. Mark **ownership** (team/person) for accountability
4. Link **related terms** and **ADRs** for navigability

## Skill Integration

The following skills read from this configuration:
- `domain-modeling` — builds/sharpens the glossary and ADRs
- `to-spec` — uses glossary terms for consistent specifications
- `codebase-design` — references ADRs for architectural context
- `implement-spec` — checks glossary for domain term usage

## Templates

### Glossary Entry Template
```markdown
## Term Name

**Definition**: One-sentence operational definition.

**Aliases**: alias1, alias2, abbreviation

**Related**: [[Other Term]], [[Another Term]]

**Owner**: @username or team-name

**ADRs**: [[0001-decision]], [[0002-another-decision]]
```

### ADR Template
```markdown
---
title: Short Title
status: Accepted | Proposed | Superseded | Deprecated
date: YYYY-MM-DD
deciders: @username1, @username2
consulted: @username3
informed: @team
---

# NNNN: Short Title

## Context
What problem are we solving? What constraints exist?

## Decision
What did we decide? Be specific.

## Consequences
### Positive
- Benefit 1
- Benefit 2

### Negative
- Trade-off 1
- Trade-off 2

## Related
- [[Glossary Term]]
- [[0001-other-adr]]
```