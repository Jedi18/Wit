package wit;

import java.io.Serializable;
import java.util.HashSet;

public class StagingData implements Serializable {
    public int n;
    public String[] stagingData;
    public String directoryHash;

    public StagingData(String directoryHash, HashSet<String> stagingSet) {
        this.directoryHash = directoryHash;
        n = stagingSet.size();
        int index = 0;
        stagingData = new String[n];
        for(String str : stagingSet) {
            stagingData[index] = str;
            index++;
        }
    }
}
