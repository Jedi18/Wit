package wit;

import java.util.HashMap;
import java.util.Map;

public class BranchManager {
    public Map<String, String> branchToCommit;
    public String head;

    public BranchManager() {
        branchToCommit = new HashMap<>();
    }
}
