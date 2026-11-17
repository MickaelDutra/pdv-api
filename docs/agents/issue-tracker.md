# Issue Tracker Configuration

## Tracker: GitHub Issues

Issues for this repository live in **GitHub Issues** at:
https://github.com/MickaelDutra/pdv-api/issues

## Tooling

- **CLI**: `gh` (GitHub CLI)
- **Authentication**: `gh auth login` (run once per machine)
- **Default workflow**: `gh issue create`, `gh issue list`, `gh issue view`, `gh issue edit`

## Conventions

- **Labels**: Managed via the triage label vocabulary (see `triage-labels.md`)
- **Projects**: Not used by default
- **Milestones**: Optional, for release planning
- **PRs as request surface**: **Disabled** — pull requests are not automatically triaged as incoming work. Enable in this file if you want external PRs to enter the triage queue.

## Common Commands

```bash
# Create an issue
gh issue create --title "Title" --body "Description" --label "needs-triage"

# List open issues with triage labels
gh issue list --label "needs-triage,needs-info,ready-for-agent,ready-for-human" --state open

# View an issue
gh issue view 123

# Add/remove labels
gh issue edit 123 --add-label "ready-for-agent"
gh issue edit 123 --remove-label "needs-triage"

# Close an issue
gh issue close 123 --reason "completed"  # or "not-planned"
```

## Skill Integration

The following skills read from this configuration:
- `to-tickets` — creates issues from a spec
- `triage` — processes the triage queue using the label vocabulary
- `to-spec` — reads issues to generate specifications