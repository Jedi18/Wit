package wit;

import java.io.Serializable;
import java.util.ArrayList;

public class Tree implements Serializable {
    public ArrayList<TreeData> dataList;

    public Tree() {
        dataList = new ArrayList<TreeData>();
    }

    public void addBlob(String blobSha, String filename) {
        dataList.add(new TreeData(blobSha, filename, TreeData.TreeDataType.Blog));
    }

    public void addTree(String treeSha, String directoryPath) {
        dataList.add(new TreeData(treeSha, directoryPath, TreeData.TreeDataType.Tree));
    }

    public void printTree() {
        for(TreeData treeData : dataList) {
            System.out.println("SHA-1 : " + treeData.sha + " Type : " + treeData.type + " Path : " + treeData.path);
        }
    }

    public boolean isEmpty() {
        return dataList.isEmpty();
    }
}
