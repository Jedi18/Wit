package wit;

import java.io.Serializable;
import java.util.Map;

public class BranchData implements Serializable {
    public Map<String, String> branchMap;
    public String head;

    public BranchData(Map<String, String> branchMap, String head) {
        this.head = head;
        this.branchMap = branchMap;
    }
}
