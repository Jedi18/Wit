package wit;

public class Main {
    public static void main(String[] args) {
	    if(args.length == 0) {
	        System.out.println("Usage :- java wit.Main ...\nType java wit.Main help for information on different commands");
	        return;
        }

	    String command = args[0];

	    if(command.equals("init")) {
            init();
        } else if (command.equals("add")) {
            if(args.length != 2) {
                System.out.println("Usage for wit add :- java wit.Main add path_to_directory/path_to_file");
                return;
            }
            WitVCS.getWit().stagePath(args[1]);
        } else if (command.equals("commit")) {

        } else if (command.equals("rm")) {

        } else if (command.equals("log")) {

        } else if (command.equals("global-log")) {

        } else if (command.equals("find")) {

        } else if (command.equals("status")) {
            WitVCS.getWit().status();
        } else if (command.equals("checkout")) {

        } else if (command.equals("branch")) {

        } else if (command.equals("rm-branch")) {

        } else if (command.equals("reset")) {

        } else if (command.equals("merge")) {

        } else if (command.equals("help")) {

        } else {
	        System.out.println("Invalid command.");
        }
    }

    private static void init() {
        if(WitVCS.initialize()) {
            System.out.println("A wit version-control system already exists in this directory.");
            WitVCS.getWit().test();
        } else {
            if(WitVCS.getWit() == null) {
                System.out.println("Wit initialization failed.");
            } else {
                System.out.println("Wit repository initialized");
            }
        }
    }
}
