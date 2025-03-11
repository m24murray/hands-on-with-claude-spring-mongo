Hey I want to create a new Java Spring REST API that sits above a mongo db.

Ask me one question at a time so we can develop a thorough, step-by-step spec for this idea. Once you're done, summarise your findings into a file called spec.md.

Each question you ask me should build on my previous answers, and our end goal is to have a detailed specification I can hand off to a developer. Let’s do this iteratively and dig into every relevant detail. Remember, only one question at a time.

Here’s the details:
* Java Spring REST API project
* Java 21
* Sits on top a mongo db, so will need dependencies pulled in
* We'll work off a basic data model, we'll have a single model called users. A user will have 3 fields, a unique id, name and email.
* REST API should have controller endpoints for CRUD operations for users.
* Standard controller, service, repository structure
* API endpoints should have an Open API spec that i can send to other teams
* Will need unit tests
* Don't worry about security/auth right now, we'll implement that later
* Use gradle for dependency mgmt.

Anything that I choose not to implement right now, you can put into a file called `future-considerations.md`.