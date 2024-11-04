package org.assignment.impl;


import org.assignment.*;
import org.assignment.exceptions.InvalidBranchException;
import org.assignment.exceptions.InconsistentCommitHistoryException;
import org.assignment.utils.BranchToShaMap;
import org.assignment.utils.CommitToParentsMap;
import org.assignment.utils.StringUtils;

import java.io.IOException;
import java.util.*;

public class LastCommonCommitsFinderImpl implements LastCommonCommitsFinder {
    private final AbstractRepositoryService repositoryService;

    public LastCommonCommitsFinderImpl(AbstractRepositoryService repositoryService) {
        this.repositoryService = repositoryService;
    }


    private LinkedHashSet<String> collectReachableCommits(String startCommitSha, CommitToParentsMap commitToParentMap) {
        var history = new LinkedHashSet<String>();
        var commitsToVisit = new ArrayDeque<String>();

        commitsToVisit.push(startCommitSha);

        while (!commitsToVisit.isEmpty()) {
            String currentCommit = commitsToVisit.pop();

            if (!history.contains(currentCommit)) {
                history.add(currentCommit);
                List<String> parents = commitToParentMap.getOrDefault(currentCommit, Collections.emptyList());

                for (String parent : parents) {
                    if (!history.contains(parent)) {
                        commitsToVisit.push(parent);
                    }
                }
            }
        }
        return history;
    }


    private Optional<String> findFirstCommonElement(LinkedHashSet<String> commitsA, LinkedHashSet<String> commitsB) {
        for (String element : commitsA) {
            if (commitsB.contains(element)) {
                return element.describeConstable();
            }
        }
        return Optional.empty();
    }


    private List<String> extractCommonSequence(String commitSha, CommitToParentsMap commitToParentMap) {
        List<String> commonSequence = new ArrayList<>();
        commonSequence.add(commitSha);

        var parents = commitToParentMap.get(commitSha);
        while (parents.size() == 1) {
            var sha = parents.getFirst();
            commonSequence.add(sha);
            parents = commitToParentMap.get(sha);
        }
        return commonSequence;
    }


    /**
     * Find the last common commits between two branches
     *
     * @param branchA The name of branch A
     * @param branchB The name of branch B
     * @return A collection of the last common commits between the two branches
     * @throws IOException If an I/O error occurs
     */
    @Override
    public Collection<String> findLastCommonCommits(String branchA, String branchB) throws IOException {
        branchA = StringUtils.stripIfExist(branchA);
        branchB = StringUtils.stripIfExist(branchB);

        try {
            if (!StringUtils.hasText(branchA) || !StringUtils.hasText(branchB)) {
                throw new IllegalArgumentException("Branch name cannot be empty");
            }

            BranchToShaMap branchesMap = repositoryService.getBranches();

            var repositoryBranches = branchesMap.keySet();
            if (!repositoryBranches.contains(branchA)) {
                throw new InvalidBranchException(branchA, repositoryBranches.stream().toList());
            }
            if (!repositoryBranches.contains(branchB)) {
                throw new InvalidBranchException(branchB, repositoryBranches.stream().toList());
            }

            var shaBranchA = branchesMap.get(branchA);
            var shaBranchB = branchesMap.get(branchB);

            CommitToParentsMap commits = repositoryService.getCommits();

            var commitSetA = collectReachableCommits(shaBranchA, commits);
            var commitSetB = collectReachableCommits(shaBranchB, commits);

            var firstCommonElement = findFirstCommonElement(commitSetA, commitSetB);
            if (firstCommonElement.isEmpty()) {
                throw new InconsistentCommitHistoryException();
            }
            var result =  extractCommonSequence(firstCommonElement.get(), commits);
            return result.reversed();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }
}