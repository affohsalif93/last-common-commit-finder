# LastCommonCommitsFinder

## Overview

**LastCommonCommitsFinder** is a Java library for finding the latest common commits between two branches in a GitHub repository. It utilizes GitHub’s REST API to traverse commit histories and identify commits that are reachable from both branches but are not reachable from any later commits also accessible by both branches.

### Key Features

- **Identify Latest Common Commits**: Finds the most recent commit SHAs that are shared between two specified branches.
- **Caching**: Caches API responses to reduce redundant GitHub API calls.
- **Private Repo Access**: Supports GitHub API access with or without a personal access token.
- **Fully Tested**: Includes comprehensive unit and integration tests, as well as robust error handling.

---

### Key Components

- **`LastCommonCommitsFinder`**: Core interface for finding the latest common commits between two branches.
- **`LastCommonCommitsFinderFactory`**: Factory interface for creating instances of `LastCommonCommitsFinder` tailored to a specific repository.
- **`LastCommonCommitsFinderImpl`**: Implements core logic for identifying common commits by traversing commit histories.
- **`GitHubRepositoryServiceImpl`**: Extends `AbstractRepositoryService` to interact with GitHub’s REST API.
- **`GithubHttpOkReponseCache`**: Provides caching functionality to minimize redundant API calls, enhancing performance by storing branch and commit data responses.
- **Tests**: Unit tests verify functionality, covering commit retrieval, error handling, and caching behavior.

---

## Setup and Installation

### Requirements

- **Java 8** or higher
- **Maven**
- GitHub Personal Access Token (optional, for private repositories or extended API rate limits)

### Installation

1. **Clone the repository**:
   ```bash
   git clone https://github.com/affohsalif93/last-common-commit-finder.git
   cd last-common-commit-finder
   ```

2. **Build the project**:
   ```bash
   mvn clean install
   ```
---

## Usage

1. **Create an Instance of LastCommonCommitsFinder**:  
   Use `LastCommonCommitsFinderFactory` to create an instance of `LastCommonCommitsFinder` for a specific GitHub repository.

   ```java
   LastCommonCommitsFinderFactory factory = new LastCommonCommitsFinderFactoryImpl();
   LastCommonCommitsFinder finder = factory.create("owner", "repo", "token");
   ```

2. **Find Last Common Commits**:  
   Call `findLastCommonCommits` with two branch names to retrieve the SHAs of the latest common commits.

   ```java
   try {
       Collection<String> commonCommits = finder.findLastCommonCommits("main", "dev");
       commonCommits.forEach(System.out::println);
   } catch (IOException e) {
       System.err.println("An error occurred: " + e.getMessage());
   }
   ```

### Example

```java
LastCommonCommitsFinderFactory factory = new LastCommonCommitsFinderFactoryImpl();
LastCommonCommitsFinder finder = factory.create("octocat", "Hello-World", null);

try {
    Collection<String> commits = finder.findLastCommonCommits("main", "feature-branch");
    System.out.println("Last common commits:");
    commits.forEach(System.out::println);
} catch (IOException e) {
    System.err.println("Failed to retrieve commits: " + e.getMessage());
}
```

---

## Caching

This library implements a basic caching mechanism through `GithubHttpOkResponseCache` to reduce the frequency of API calls. Cached data includes `HTTP_OK` responses for branch and commit histories.

---

## Testing

The library includes comprehensive tests covering:

- **Core functionality**: Ensures `findLastCommonCommits` accurately retrieves commit SHAs.
- **Error handling**: Tests handling of common API errors, such as network issues and rate limits.
- **Caching**: Verifies cache utilization and expiration.


### Test Setup
To run integration tests and access private repositories, set the following environment variables:

```bash
export GITHUB_TOKEN=your_personal_access_token
export REPO_OWNER=your_repository_owner
export REPO_NAME=your_repository_name
```

To run tests:

```bash
mvn test  
```

---

## Possible Improvements and Future Ideas

- **Enhanced Testing**: Add test cases for network-related errors and API retry limits.
- **Failed Request Caching**: Use the status code of failed requests to avoid redundant API calls for requests unlikely to succeed immediately.
- **Multiple Branch Comparison**: Extend functionality to compare more than two branches.
- **Command-line Utility**: Develop a command-line tool to use this library more easily.

---