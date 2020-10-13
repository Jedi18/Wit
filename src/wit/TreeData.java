package wit;

import java.io.Serializable;

public class TreeData implements Serializable {
    public enum TreeDataType {
        Blog,
        Tree
    }

    public String sha;
    public String path;
    public TreeDataType type;

    public TreeData(String sha, String path, TreeDataType treeDataType) {
        this.sha = sha;
        this.path = path;
        this.type = treeDataType;
    }
}
