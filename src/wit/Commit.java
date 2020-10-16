package wit;

import java.io.Serializable;
import java.util.Date;

public class Commit implements Serializable {
    Date commitDate;
    String author;
    String treeHash;
    String parentCommitHash;

    public Commit(String treeHash, String author, Date commitDate, String parentCommitHash) {
        this.treeHash = treeHash;
        this.author = author;
        this.commitDate = commitDate;
        this.parentCommitHash =  parentCommitHash;
    }
}
