package wit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/** WitVCS class
 *  @author Jedi18
 */
public class WitVCS {
    private static WitVCS instance = null;
    File witFile = null;
    File repoFile = null;

    public WitVCS(File witFile, String directoryPath) {
        this.witFile = witFile;
        this.repoFile = new File(directoryPath);
    }

    /* Initialize git repository */
    public static boolean initialize() {
        //String directoryPath = System.getProperty("user.dir");
        String directoryPath = "C:\\Users\\targe\\Documents\\wittest";
        File witPath = new File(directoryPath + "/.wit");

        if(Files.exists(witPath.toPath())) {
            instance = new WitVCS(witPath, directoryPath);
            return true;
        }

        try {
            Files.createDirectory(witPath.toPath());
            instance = new WitVCS(witPath, directoryPath);
        } catch (IOException ioException) {
            System.out.println("Could not create .wit directory.");
        }

        return false;
    }

    public void test() {
        generateTree(processDirectory(repoFile), new File(witFile.getAbsolutePath() + "\\testFolder"));
    }

    /* Generate tree to the given directory */
    private void generateTree(String treeHash, File directory) {
        File tree1 = new File(witFile.getAbsolutePath() + "\\" + treeHash);
        Tree tree = Utils.readObject(tree1, Tree.class);

        tree.printTree();
        directory.mkdir();

        for(TreeData td : tree.dataList) {
            if(td.type == TreeData.TreeDataType.Tree) {
                generateTree(td.sha, new File(directory.getAbsolutePath() + "\\" + td.path));
            }else if(td.type == TreeData.TreeDataType.Blog) {
                File contentFile = new File(directory.getAbsolutePath() + "\\" + td.path);
                File blobFile = new File(witFile.getAbsolutePath() + "\\" + td.sha);
                byte[] content = Utils.readContents(blobFile);
                try {
                    contentFile.createNewFile();
                    Utils.writeContents(contentFile, content);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
    }

    private String processDirectory(File directory) {
        String contents[] = directory.list();
        Tree tree = new Tree();

        for (String content : contents){
            if(content.equals(".wit")) {
                continue;
            }

            File contentFile = new File(directory.getAbsolutePath() + "\\" + content);
            if(contentFile.isFile()) {
                byte[] contentData = Utils.readContents(contentFile);
                String contentSha = Utils.computeSHA1(contentData);

                File contentBlob = new File(witFile.getAbsolutePath() + "\\" + contentSha);
                if(!contentBlob.exists()) {
                    try {
                        contentBlob.createNewFile();
                        Utils.writeContents(contentBlob, contentData);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e.getMessage());
                    }
                }

                tree.addBlob(contentSha, contentFile.getName());
            }

            if(contentFile.isDirectory()) {
                String directorySha = processDirectory(contentFile);
                tree.addTree(directorySha, contentFile.getName());
            }
        }

        byte[] contentTreeData = Utils.serialize(tree);
        String contentTreeSha = Utils.computeSHA1(contentTreeData);
        File contentTree = new File(witFile.getAbsolutePath() + "\\" + contentTreeSha);
        if(!contentTree.exists()) {
            try {
                contentTree.createNewFile();
                Utils.writeContents(contentTree, contentTreeData);
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        return contentTreeSha;
    }

    public static WitVCS getWit() {
        return instance;
    }
}
