package wit;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.nio.file.Files;
import java.nio.file.Path;

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
        String contents[] = repoFile.list();

        for (String content : contents){
            if(content.equals(".wit")) {
                continue;
            }
            File contentFile = new File(repoFile.getAbsolutePath() + "\\" + content);
            if(contentFile.isFile()) {
                byte[] contentData = Utils.readContents(contentFile);
                System.out.println("SHA-1 value for " + content + " is " + Utils.computeSHA1(contentData));
            }
        }
    }

    public static WitVCS getWit() {
        return instance;
    }
}
