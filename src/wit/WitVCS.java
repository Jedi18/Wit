package wit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Date;

/** WitVCS class
 *  @author Jedi18
 */
public class WitVCS {
    private static WitVCS instance = null;
    File witFile = null;
    File repoFile = null;
    StagingArea stagingArea;
    BranchManager branchManager;

    private String author = "Jedi18";

    public WitVCS(File witFile, String directoryPath) {
        this.witFile = witFile;
        this.repoFile = new File(directoryPath);
        this.stagingArea = new StagingArea(witFile);
        this.branchManager = new BranchManager(witFile);
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
            File metadataFolder = new File(witPath.toPath() + "\\metadata");
            metadataFolder.mkdir();

            instance = new WitVCS(witPath, directoryPath);
        } catch (IOException ioException) {
            System.out.println("Could not create .wit directory.");
        }

        return false;
    }

    public void test() {
        stagingArea.addDirectory(repoFile);
        String directoryHash = stagingArea.getTreeHash(stagingArea.processDirectory(repoFile));
        generateTree(directoryHash, new File(witFile.getAbsolutePath() + "\\testFolder"));
    }

    public void stagePath(String path) {
        File stageFile = new File(path);

        if(stageFile.isDirectory()) {
            stagingArea.addDirectory(stageFile);
        }
    }

    public void status() {
        stagingArea.printStatus();
    }

    public void checkout(String branchName) {
        String commitSha = branchManager.getBranchCommit(branchName);

        if(commitSha == null) {
            System.out.println("Branch of given name " + branchName + " does not exist.");
            return;
        }

        Commit commit = readCommit(commitSha);
        generateTree(commit.treeHash, new File(witFile.getAbsolutePath() + "\\testFolder2"));
    }

    private Commit readCommit(String commitSha) {
        File commitFile = new File(witFile.getAbsolutePath() + "\\" + commitSha);
        Commit commit = Utils.readObject(commitFile, Commit.class);
        return commit;
    }

    public void processCommit() {
        String directoryHash = stagingArea.getTreeHash(stagingArea.processDirectory(repoFile));
        Commit commit = new Commit(directoryHash, author, new Date(), "null");
        String commitSha = getCommitHash(commit);
        branchManager.updateCommit(commitSha);
    }

    protected String getCommitHash(Commit commit) {
        byte[] commitData = Utils.serialize(commit);
        String commitSha = Utils.computeSHA1(commitData);
        File commitFile = new File(witFile.getAbsolutePath() + "\\" + commitSha);
        if(!commitFile.exists()) {
            try {
                commitFile.createNewFile();
                Utils.writeContents(commitFile, commitData);
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        return commitSha;
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

    public static WitVCS getWit() {
        return instance;
    }
}
