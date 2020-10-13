package wit;

import java.io.Serializable;
import java.util.Date;

public class Commit implements Serializable {
    Date commitDate;
    String author;
    String treeHash;

    public Commit(String treeHash, String author, Date commitDate) {
        this.treeHash = treeHash;
        this.author = author;
        this.commitDate = commitDate;
    }
}
