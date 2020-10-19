package wit;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class BranchManager {
    public Map<String, String> branchToCommit;
    public String head;
    File witFile = null;

    public BranchManager(File witFile) {
        this.witFile = witFile;
        branchToCommit = new HashMap<>();
        head = "null";
        readBranchData();
    }

    void updateCommit(String commitSha) {
        if(head == "null") {
            head = "master";
        }

        branchToCommit.put(head, commitSha);
        writeBranchData();
    }

    String getBranchCommit(String branchName) {
        if(!branchToCommit.containsKey(branchName)) {
            return null;
        }

        return branchToCommit.get(branchName);
    }

    private void readBranchData() {
        File branchDataFile = new File(witFile.getAbsolutePath() + "\\metadata\\branchData");

        if(branchDataFile.exists()) {
            BranchData branchData = Utils.readObject(branchDataFile, BranchData.class);
            this.branchToCommit = branchData.branchMap;
            this.head = head;
        }else{
            writeBranchData();
        }
    }

    private void writeBranchData() {
        File branchDataFile = new File(witFile.getAbsolutePath() + "\\metadata\\branchData");

        if(!branchDataFile.exists()) {
            try{
                branchDataFile.createNewFile();
            }catch(IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        BranchData branchData = new BranchData(this.branchToCommit, this.head);
        Utils.writeObject(branchDataFile, branchData);
    }
}
