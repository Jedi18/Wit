package wit;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
        processDirectory(repoFile);
    }

    private void processDirectory(File directory) {
        String contents[] = directory.list();

        for (String content : contents){
            System.out.println("Content : " + content);
            if(content.equals(".wit")) {
                continue;
            }

            File contentFile = new File(directory.getAbsolutePath() + "\\" + content);
            if(contentFile.isFile()) {
                byte[] contentData = Utils.readContents(contentFile);
                String contentSha = Utils.computeSHA1(contentData);
                System.out.println("SHA-1 value for " + content + " is " + contentSha);

                File contentBlob = new File(witFile.getAbsolutePath() + "\\" + contentSha);
                if(contentBlob.exists()) {
                    continue;
                }else{
                    try {
                        contentBlob.createNewFile();
                        Utils.writeContents(contentBlob, contentData);
                    } catch (IOException e) {
                        throw new IllegalArgumentException(e.getMessage());
                    }
                }
            }

            if(contentFile.isDirectory()) {
                System.out.println(content + " is a directory");
                processDirectory(contentFile);
            }
        }
    }

    public static WitVCS getWit() {
        return instance;
    }
}
