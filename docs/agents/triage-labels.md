# Triage Label Vocabulary

This file maps the five canonical triage roles to their actual label strings in GitHub Issues.

## Label Mapping

| Canonical Role | GitHub Label | Description |
|----------------|--------------|-------------|
| `needs-triage` | `needs-triage` | New issue awaiting initial assessment |
| `needs-info` | `needs-info` | Blocked on clarification from reporter or stakeholder |
| `ready-for-agent` | `ready-for-agent` | Well-defined, actionable — an agent can pick it up |
| `ready-for-human` | `ready-for-human` | Requires human judgment, design decision, or external dependency |
| `wontfix` | `wontfix` | Explicitly declined; no action will be taken |

## Usage Rules

1. **Every issue must have exactly one triage label** at all times.
2. **Transition flow**: `needs-triage` → (`needs-info` | `ready-for-agent` | `ready-for-human` | `wontfix`)
3. **`needs-info`** is a holding state — the issue returns to `needs-triage` once info arrives.
4. **`ready-for-agent`** means the issue has:
   - Clear acceptance criteria
   - No blocking dependencies
   - Sufficient context for an autonomous agent
5. **`ready-for-human`** means the issue needs:
   - Architectural decision
   - Stakeholder input
   - Credentials/secrets only a human can provide
   - Design/UX judgment
6. **`wontfix`** is terminal — do not reopen without explicit human approval.

## GitHub Label Management

These labels should exist in the repository. Create missing ones with:

```bash
gh label create "needs-triage" --color "FBCA04" --description "Awaiting initial assessment"
gh label create "needs-info" --color "D4C5F9" --description "Blocked on clarification"
gh label create "ready-for-agent" --color "0E8A16" --description "Actionable for autonomous agent"
gh label create "ready-for-human" --color "1D76DB" --description "Requires human judgment"
gh label create "wontfix" --color "FFFFFF" --description "Explicitly declined"
```

## Skill Integration

The `triage` skill reads this file to:
- Know which labels to apply/look for
- Enforce the transition rules
- Route issues to the correct queue (agent vs human)