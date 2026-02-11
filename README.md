# Assignment #2: Continuous Integration Server

Java implementation of a Continuous Integration (CI) server for the second lab assignment in the course DD2480.

## Description

This project implements a CI server that receives GitHub webhooks on push events. It clones the repository branch, runs the test suite with Maven (`mvnw test`), stores build logs, and notifies the commit status back to GitHub. The server handles POST (webhook) and GET (e.g. build history) requests.

## Dependencies

> [!IMPORTANT]
> The following dependency versions have been verified to build this project. Use any other version at your own risk.

* **Java SDK**: Version 21
* **Apache Maven**: Use the Maven Wrapper (`./mvnw`) included in the project.
* **JUnit**: Version 5.10.0 (managed via Maven).
* **Jetty**: Version 11.0.20.

## Configuration

Create a `.env` file in the project root with your GitHub token:

```
GITHUB_ACCESS_TOKEN=your_github_personal_access_token
```
You can generate one at https://github.com/settings/tokens.

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

To get the build history you can visit http://localhost:8080/builds

## Implementation

### Notifications

#### GitHub

Notifications to GitHub are sent by calling the GitHub API with a JSON object containing information from the build process. Once a build is complete, its status is reported via the `notify` function in the `GitHubClient` class. It passes the build status as the GitHub state and the URL of the build log for the "details" link.

#### Email 

Email notifications use the Gmail SMTP server. Notifications are sent to the email address of the person responsible for pushing to the repository. The email includes the build result, the specific commit SHA and a link to the build log. This is implemented in the `EmailNotifier` class.

> [!IMPORTANT]
> Users must disable "Keep my email addresses private" in their GitHub settings to be able to receive these emails.

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
