# Project context

This repository is a starter template for a greenfield Java project used in an introductory software engineering course in an undergraduate computer science program. Students use it as the starting point for their own projects.

# Default user context

Unless the user says otherwise, assume that you are assisting a student working on a project in this repository. If the user identifies themselves as an instructor or another project stakeholder, adapt your response to that role.

# Student profile

* Prior knowledge: Basic Java and OOP concepts.
* Level of programming experience: Intermediate
* IDE and level of expertise: VSCode, Intermediate
# Guidance for interacting with users

* Explain the rationale for significant actions: what you did and why.
* Keep explanations brief but instructive, supporting learning through responsible use of AI. For example:

  * When suggesting a Git command, briefly explain what it does.
  * Add explanatory Javadoc comments to all classes and to nontrivial methods and fields when their purpose or behavior is not obvious.
  * Make generated code as self-explanatory as possible, and include explanatory comments where they improve understanding.
  * When faced with a design choice, choose the simplest option that is sufficient for the requirements, while briefly explaining relevant more advanced alternatives.

# Project-specific requirements

## Java version:

Ensure that Java 25 is used when running the application or build tasks. On macOS, use `sdk use java 25.0.3.fx-zulu` to switch to Java 25 if needed.

## Java coding standard

All Java code in this project **must** follow the SE-Education intermediate
Java coding standard. The full rules are captured in the
`seedu-java-coding-standard` skill (`.claude/skills/seedu-java-coding-standard/SKILL.md`).

Before reporting any Java code task as complete, verify the touched files
against that skill's checklist. Key points to watch:

* Every class must have a `package` declaration (Rule 27).
* No wildcard imports (Rule 29).
* Always use braces for single-statement `if`/`for`/`while` bodies (Rules 33--35).
* K&R brace style throughout (Rule 18).
* 4-space indentation, no tabs (Rule 13).
* Line length hard limit 120 characters (Rule 14).

Reference: <https://se-education.org/guides/conventions/java/intermediate.html>

## Git

Use lightweight tags unless the user requests an annotated tag.
When proposing or creating a commit message, include enough detail to explain the rationale for the change.
Do not commit or push unless explicitly asked.
