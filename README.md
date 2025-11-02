# Virtual Scroll Access System (VSAS)

> **Group A3-T10-G09** · SOFT2412 2025 S2  
> A command-line app that simulates a virtual scroll library for Whiskers to upload, search, preview, and download scrolls.

---

## Team

| Role          | Name                      | SID       |
| ------------- | ------------------------- | --------- |
| Product Owner | **Juan David Lopez Arce** | 540153206 |
| Scrum Master  | **Alex Daitz**            | 540718728 |
| Developer     | **Kenton Lee**            | 540745333 |
| Developer     | **Kegan Hofmeyr**         | 540754690 |

---

## Prerequisites

- Git
- Java (JDK)
- Gradle wrapper (included as `./gradlew`)

---

## Installation & Setup

```bash
# Clone repository
git clone https://github.sydney.edu.au/SOFT2412-COMP9412-2025s2/A3-T10-G09.git
cd A3-T10-G09
```

---

## Build, Test, Run

### Build

```bash
./gradlew build
```

### Test

```bash
./gradlew test
# Coverage report:
./gradlew jacocoTestReport
# Open: build/reports/jacoco/index.html
```

### Run (standard)

Use Gradle to launch with a clean, plain console:

```bash
./gradlew run --console=plain
```

Follow the CLI prompts.

---

## Password Masking (CLI)

Some terminals launched via Gradle don’t provide a fully interactive console. If you want **masked password input**, run the **installed distribution** instead of `gradlew run`.

```bash
# Build and install the distribution
./gradlew installDist

# Run the installed app (enables proper no-echo input on most terminals)
./app/build/install/app/bin/app
```

> Tip: For all other functionality, `./gradlew run --console=plain` is fine if masking isn’t required.

---

## Quick Start

```bash
# Build
./gradlew build

# (Option A) Run with Gradle (plain console)
./gradlew run --console=plain

# (Option B) Run with installed distribution (for password masking)
./gradlew installDist
./app/build/install/app/bin/app

# Tests
./gradlew test

# Troubleshooting
./gradlew clean
```

---

## Notes

- Data is stored locally in JSON (no external DB required).
- Keep feature branches small and open PRs to the blessed repo for review.
