package wit;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class StagingArea {
    private Set<String> stagedFiles;
    File witFile = null;

    public StagingArea(File witFile) {
        this.witFile = witFile;
        this.stagedFiles = new HashSet<>();
    }

    public void addFile(File file) {
        stagedFiles.add(file.getAbsolutePath());
    }

    public void addDirectory(File dir) {
        String contents[] = dir.list();

        for (String content : contents){
            if(content.equals(".wit")) {
                continue;
            }

            File contentFile = new File(dir.getAbsolutePath() + "\\" + content);
            if(contentFile.isFile()) {
                stagedFiles.add(contentFile.getAbsolutePath());
            }

            if(contentFile.isDirectory()) {
                addDirectory(contentFile);
            }
        }
    }

    protected String getTreeHash(Tree tree) {
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

    protected Tree processDirectory(File directory) {
        String contents[] = directory.list();
        Tree tree = new Tree();

        for (String content : contents){
            if(content.equals(".wit")) {
                continue;
            }

            File contentFile = new File(directory.getAbsolutePath() + "\\" + content);
            if(contentFile.isFile()) {
                if(stagedFiles.contains(contentFile.getAbsolutePath())) {
                    byte[] contentData = Utils.readContents(contentFile);
                    String contentSha = Utils.computeSHA1(contentData);

                    File contentBlob = new File(witFile.getAbsolutePath() + "\\" + contentSha);
                    if (!contentBlob.exists()) {
                        try {
                            contentBlob.createNewFile();
                            Utils.writeContents(contentBlob, contentData);
                        } catch (IOException e) {
                            throw new IllegalArgumentException(e.getMessage());
                        }
                    }

                    tree.addBlob(contentSha, contentFile.getName());
                }
            }

            if(contentFile.isDirectory()) {
                Tree innerDirectory = processDirectory(contentFile);
                if(!innerDirectory.isEmpty()) {
                    String directorySha = getTreeHash(innerDirectory);
                    tree.addTree(directorySha, contentFile.getName());
                }
            }
        }

        return tree;
    }
}
