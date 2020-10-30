package wit;

import java.io.File;
import java.io.Serializable;
import java.util.Date;

public class Commit implements Serializable {
    Date commitDate;
    String author;
    String treeHash;
    String parentCommitHash;
    String message;

    public Commit(String treeHash, String message, String author, Date commitDate, String parentCommitHash) {
        this.treeHash = treeHash;
        this.author = author;
        this.commitDate = commitDate;
        this.message = message;
        this.parentCommitHash =  parentCommitHash;
    }

    public static Commit readCommit(File witFile, String commitSha) {
        if(commitSha == null) {
            return null;
        }

        File commitFile = new File(witFile.getAbsolutePath() + "\\" + commitSha);
        Commit commit = Utils.readObject(commitFile, Commit.class);
        return commit;
    }
}
