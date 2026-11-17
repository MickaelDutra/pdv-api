# Agent Instructions for PDV API

This file configures how AI agents should work with this repository.

## Agent skills

### Issue tracker

Issues are tracked in GitHub Issues (https://github.com/MickaelDutra/pdv-api/issues) using the `gh` CLI. See `docs/agents/issue-tracker.md`.

### Triage labels

Five canonical triage labels map directly to GitHub labels: `needs-triage`, `needs-info`, `ready-for-agent`, `ready-for-human`, `wontfix`. See `docs/agents/triage-labels.md`.

### Domain docs

Single-context layout: one `GLOSSARY.md` + `docs/adr/` at the repo root. See `docs/agents/domain.md`.