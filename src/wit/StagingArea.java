package wit;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class StagingArea {
    private HashSet<String> stagedFiles;
    File witFile = null;
    String currentStagedDirectoryHash;

    public StagingArea(File witFile) {
        this.witFile = witFile;
        readStageFile();
    }

    public void addFile(File file) {
        stagedFiles.add(file.getAbsolutePath());
        writeToStageFile();
    }

    public void addDirectory(File dir) {
        String contents[] = dir.list();

        for (String content : contents) {
            if (content.equals(".wit")) {
                continue;
            }

            File contentFile = new File(dir.getAbsolutePath() + "\\" + content);
            if (contentFile.isFile()) {
                stagedFiles.add(contentFile.getAbsolutePath());
            }

            if (contentFile.isDirectory()) {
                addDirectory(contentFile);
            }
        }
    }

    protected void printStatus() {
        System.out.println("Staged Files : - ");
        for(String stagedFile : stagedFiles) {
            System.out.println(stagedFile);
        }
    }

    protected void writeToStageFile() {
        File stageFile = new File(witFile.getAbsolutePath() + "\\metadata\\stageData");
        if(!stageFile.exists()) {
            try{
                stageFile.createNewFile();
            } catch (IOException e) {
                throw new IllegalArgumentException(e.getMessage());
            }
        }

        StagingData stagingData = new StagingData(currentStagedDirectoryHash, stagedFiles);
        Utils.writeContents(stageFile, Utils.serialize(stagingData));
    }

    private void readStageFile() {
        File stageFile = new File(witFile.getAbsolutePath() + "\\metadata\\stageData");
        this.stagedFiles = new HashSet<>();

        if(stageFile.exists()) {
            StagingData stagingData = Utils.readObject(stageFile, StagingData.class);
            for(int i = 0; i < stagingData.n; i++) {
                this.stagedFiles.add(stagingData.stagingData[i]);
            }
            this.currentStagedDirectoryHash = stagingData.directoryHash;
        }
    }

    protected void clear() {
        stagedFiles.clear();
        currentStagedDirectoryHash = null;
        writeToStageFile();
    }

    protected String getTreeHash(Tree tree) {
        byte[] contentTreeData = Utils.serialize(tree);
        String contentTreeSha = Utils.computeSHA1(contentTreeData);
        File contentTree = new File(witFile.getAbsolutePath() + "\\stagedData\\" + contentTreeSha);
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

        File stagedDataFolder = new File(witFile.getAbsolutePath() + "\\stagedData");
        if(!stagedDataFolder.exists()) {
            stagedDataFolder.mkdir();
        }

        for (String content : contents){
            if(content.equals(".wit")) {
                continue;
            }

            File contentFile = new File(directory.getAbsolutePath() + "\\" + content);
            if(contentFile.isFile()) {
                if(stagedFiles.contains(contentFile.getAbsolutePath())) {
                    byte[] contentData = Utils.readContents(contentFile);
                    String contentSha = Utils.computeSHA1(contentData);

                    File contentBlob = new File(witFile.getAbsolutePath() + "\\stagedData\\" + contentSha);
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

    protected void updateDirectoryHash(String directoryHash) {
        currentStagedDirectoryHash = directoryHash;
    }

    protected String getStagedDirectoryHash() {
        return currentStagedDirectoryHash;
    }

    protected void moveDataFromStagedToCommit() {
        File stagedDataFolder = new File(witFile.getAbsolutePath() + "\\stagedData");
        String contents[] = stagedDataFolder.list();

        try {
            for(String content : contents) {
                File oldFile = new File(stagedDataFolder.getAbsolutePath() + "\\" + content);
                File newFile = new File(witFile.getAbsolutePath() + "\\" + content);

                if(newFile.exists()) {
                    continue;
                }

                System.out.println("Copying file " + content);
                Files.copy(oldFile.toPath(), newFile.toPath());
                oldFile.delete();
            }
        } catch (IOException e) {
            System.out.println("Error while copying files from staged data folder to commit.");
            e.printStackTrace();
        }
    }
}
