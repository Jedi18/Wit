package wit;

import java.io.Serializable;
import java.util.HashSet;

public class StagingData implements Serializable {
    public int n;
    public String[] stagingData;

    public StagingData(HashSet<String> stagingSet) {
        n = stagingSet.size();
        int index = 0;
        stagingData = new String[n];
        for(String str : stagingSet) {
            stagingData[index] = str;
            index++;
        }
    }
}
