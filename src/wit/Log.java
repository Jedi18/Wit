package wit;

import java.io.File;
import java.util.HashSet;
import java.util.Map;

/* Print status and commit log information */
public class Log {
    protected static void log(File witFile, String headCommit) {
        Commit commit = Commit.readCommit(witFile, headCommit);
        String commitSha = headCommit;

        while(commit != null) {
            System.out.println("===");
            System.out.println("commit " + commitSha);
            System.out.println("Date : " + commit.commitDate.toString());
            System.out.println("Author : " + commit.author);
            System.out.println(commit.message + "\n");

            commitSha = commit.parentCommitHash;
            commit = Commit.readCommit(witFile, commit.parentCommitHash);
        }
    }

    protected static void global_log(File witFile, BranchManager branchManager) {
       for(String branchName : branchManager.branchToCommit.keySet()) {
           System.out.println("Branch : " + branchName);
           log(witFile, branchManager.getBranchCommit(branchName));
       }
    }

    protected static void status(BranchManager branchManager, StagingArea stagingArea) {
        System.out.println("=== Branches ===");
        for(String branchName : branchManager.branchToCommit.keySet()) {
            if(branchName.equals(branchManager.head)) {
                System.out.print("*");
            }
            System.out.println(branchName);
        }

        System.out.println("\n=== Staged Files ===");
        for(String fileName : stagingArea.stagedFiles) {
            System.out.println(fileName);
        }
    }
}
