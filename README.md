# Assignment #2: Continuous Integration Server

Java implementation of a Continuous Integration (CI) server for the second lab assignment in the course DD2480.

## Description

This project implements a CI server that receives GitHub webhooks on push events. It clones the repository branch, runs the test suite with Maven (`mvnw test`), stores build logs, and notifies the commit status back to GitHub. The server handles POST (webhook) and GET (e.g. build history) requests.

## Dependencies

> [!IMPORTANT]
> The following dependency versions have been verified to build this project. Use any other version at your own risk.

* **Java SDK**: Version 21
* **Apache Maven**: Use the Maven Wrapper (`./mvnw`) included in the project.
* **Jetty**: Version 11.0.20.
* **org.json**: Version 20240205 (JSON parsing).
* **dotenv-java** (io.github.cdimascio): Version 3.0.0 (`.env` configuration).
* **JUnit**: Version 5.10.0 (managed via Maven).
* **Mockito**: Version 5.11.0 (mockito-core, mockito-junit-jupiter, for testing).
* **Simple Java Mail** (org.simplejavamail): Version 8.12.6 (email notifications).

## Configuration

Create a `.env` file in the project root with the following variables:

```
GITHUB_ACCESS_TOKEN=your_github_personal_access_token
SENDER_EMAIL=your_email@gmail.com
EMAIL_PASS=your_email_password
```

* **GITHUB_ACCESS_TOKEN**: Required for the CI server to set commit status on GitHub. Generate one at https://github.com/settings/tokens.
* **SENDER_EMAIL**: The email address used to send build notifications 
* **EMAIL_PASS**: The password for the sender account.

Make sure your ssh key is linked to your github account to allow the server to clone the repo.

We advise using ngrok to forward `localhost:8080`.
Configure your GitHub repository webhook to point to your server URL (e.g. `http://your-host:8080` or the ngrok url if used it).
## How to Run

This project uses the Maven Wrapper. You do **not** need Maven installed globally; you only need Java 21 configured in your PATH.

### 1. Build the Project

To compile the code and download dependencies:

```bash
./mvnw clean compile
```

On Windows:

```bash
mvnw.cmd clean compile
```

### 2. Run the Tests

```bash
./mvnw test
```

### 3. Run the CI Server

Run the main class `ContinuousIntegrationServer` from your IDE, or with maven:

```bash
./mvnw "exec:java" "-Dexec.mainClass=ContinuousIntegrationServer"
```


The server listens on port 8080. 
You can then push files to github and the server will update the commit status according to the tests.

To get the build history you can visit https://nonnoumenal-hyo-unorbed.ngrok-free.dev/builds/

### 4. API documentation (Javadoc)

Generate the browsable API docs with `./mvnw javadoc:javadoc`.
Open **`target/site/apidocs/index.html`** in a browser to view it.
    
## Implementation

Compilation, test execution and notifications are triggered by webhook (POST). The branch to build is taken from the HTTP payload (`ref` field). The flow is: parse payload -> clone that branch -> compile & run tests -> notify.

### Compilation and test execution

The CI server uses `MavenTestExecutor`, which runs `mvnw test`. That Maven command first runs the compile phase, so the build fails on syntax errors, then runs the project’s automated tests. Compilation and test execution are handled in a single step, on the branch specified in the webhook payload.

**Flow:** On webhook, `PayloadParser` reads the payload and extracts the branch and repository data. `BranchCloner` clones that branch into a temporary directory. `MavenTestExecutor.run(projectRoot, logFile)` is then invoked in that directory; logs are written to `build_history/<commitHash>/log.txt`.

**Unit testing:** `PayloadParserTest` checks that branch, repo URL, commit hash and pusher are parsed correctly from a sample GitHub payload. `BranchClonerTest` checks cloning behaviour with a real or mocked `ProcessRunner`. `MavenTestExecutorTest` checks that `MavenTestExecutor.run()` returns `false` when the project root is missing or has no `mvnw`, and returns `true` when run on a minimal Maven project with a passing test (covering both compilation and test execution).

### Notifications

The CI server notifies build results via GitHub commit status and by email.

#### GitHub

Notifications to GitHub are sent by calling the GitHub API with a JSON object containing information from the build process. Once a build is complete, its status is reported via the `notify` function in the `GitHubClient` class. It passes the build status as the GitHub state and the URL of the build log for the "details" link.

#### Email 

Email notifications use the Gmail SMTP server. Notifications are sent to the email address of the person responsible for pushing to the repository. The email includes the build result, the specific commit SHA and a link to the build log. This is implemented in the `EmailNotifier` class.

> [!IMPORTANT]
> Users must disable "Keep my email addresses private" in their GitHub settings to be able to receive these emails.

**Unit testing:** `GitHubClientTest` checks that the JSON body for the commit status has the correct `state` and `description` for success and failure, and that the API call succeeds when the server returns 201. `EmailNotifierTest` checks that notification succeeds with valid parameters (with mocked `performSend`) and that invalid or missing email is handled correctly.

## Statement of Contributions

**Oskar**
* `GitHubNotifier`
* Server flow 
* Frontend for persistent build logs 
* Project chores (dependencies, `.gitignore`, `.editorconfig`)

**Elin**
* `EmailNotifier`
* Notification section of README 
* Class and function documentation

**Ben**
* JSON Parsing
* Code refactoring
* ESSENCE file

**Ali**
* `MavenTestExecutor` 
* README 
* Code refactoring 
* Test fixes 

**Markus**
* `BranchCloner`
* `DefaultProcessRunner`
* `ProcessRunner`
* `DirectoryRemover`
* `PayloadParser`
* Code refactoring
