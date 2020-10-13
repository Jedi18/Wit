package wit;

import java.io.Serializable;
import java.util.ArrayList;

public class Tree implements Serializable {
    ArrayList<TreeData> dataList;

    public void Tree() {

    }

    public void addBlob(String blobSha, String filename) {
        dataList.add(new TreeData(blobSha, filename, TreeData.TreeDataType.Blog));
    }

    public void addTree(String treeSha, String directoryPath) {
        dataList.add(new TreeData(treeSha, directoryPath, TreeData.TreeDataType.Tree));
    }
}
