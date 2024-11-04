package org.assignment;

import org.assignment.exceptions.*;
import org.assignment.utils.BranchToShaMap;
import org.assignment.utils.CommitToParentsMap;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;


public abstract class AbstractRepositoryService {

    protected abstract GithubHttpResponse fetchBranches() throws IOException, InterruptedException;

    protected abstract GithubHttpResponse fetchCommits() throws IOException, InterruptedException;

    /**
     * Parse the branches from the result string
     *
     * @param branchesResultString The result string from fetching branches
     * @return A map of branch names to commit SHAs
     * @throws ParsingException if there is an error parsing the branches
     */
    protected BranchToShaMap parseBranches(String branchesResultString) throws ParsingException {
        var branchesMap = new BranchToShaMap();
        try {
            var jsonBranchesArray = new JSONArray(branchesResultString);
            for (int i = 0; i < jsonBranchesArray.length(); i++) {
                var jsonBranchObject = jsonBranchesArray.getJSONObject(i);

                var commitSha = jsonBranchObject.getJSONObject("commit").getString("sha");
                var commitName = jsonBranchObject.getString("name");
                // Name -> Sha
                branchesMap.put(commitName, commitSha);
                // Sha -> Name
                branchesMap.put(commitSha, commitName);
            }
        } catch (Exception e) {
            throw new ParsingException("Error parsing branches");
        }
        return branchesMap;
    }


    /**
     * Parse the commits from the result string
     *
     * @param commitsResultString The result string from fetching commits
     * @return A map of commit SHAs to their parent commit SHAs
     * @throws ParsingException if there is an error parsing the commits
     */
    protected CommitToParentsMap parseCommits(String commitsResultString) throws ParsingException {
        var commitsMap = new CommitToParentsMap();
        try {
            var jsonCommitsArray = new JSONArray(commitsResultString);
            for (int i = 0; i < jsonCommitsArray.length(); i++) {
                JSONObject commitObject = jsonCommitsArray.getJSONObject(i);
                String commitSha = commitObject.getString("sha");

                var parentCommitsArray = commitObject.getJSONArray("parents");
                var parentCommitShaList = new ArrayList<String>();
                String parentCommitSha = null;
                if (!parentCommitsArray.isEmpty()) {
                    for (int j = 0; j < parentCommitsArray.length(); j++) {
                        var parentCommit = parentCommitsArray.getJSONObject(j);
                        parentCommitShaList.add(parentCommit.getString("sha"));
                    }
                }
                commitsMap.put(commitSha, parentCommitShaList);
            }
        } catch (Exception e) {
            throw new ParsingException("Error parsing commits");
        }
        return commitsMap;
    }


    public BranchToShaMap getBranches() throws IOException {
        try {
            var branchesResponse = fetchBranches();
            GithubHttpResponse.validateHttpResponse(branchesResponse);
            return parseBranches(branchesResponse.body());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public CommitToParentsMap getCommits() throws IOException {
        try {
            var commitsResponse = fetchCommits();
            GithubHttpResponse.validateHttpResponse(commitsResponse);
            return parseCommits(commitsResponse.body());
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}
