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
        } else if (command.equals("help")) {

        } else{
	        if(!WitVCS.initialized()) {
	            System.out.println("This directory does not contain a Wit repository. Run init command to initialize.");
	            return;
            }

            if (command.equals("add")) {
                if(printRequiredArgs(args.length, 2, "add", "path_to_directory/path_to_file")){
                    return;
                }
                WitVCS.getWit().stagePath(args[1]);
            } else if (command.equals("commit")) {
                if(printRequiredArgs(args.length, 2, "commit", "message")){
                    return;
                }

                WitVCS.getWit().processCommit(args[1]);
            } else if (command.equals("rm")) {

            } else if (command.equals("log")) {
                if(printRequiredArgs(args.length, 1, "log")){
                    return;
                }

                WitVCS.getWit().printLog();
            } else if (command.equals("global-log")) {
                if(printRequiredArgs(args.length, 1, "global-log")){
                    return;
                }

                WitVCS.getWit().printGlobalLog();
            } else if (command.equals("find")) {

            } else if (command.equals("status")) {
                WitVCS.getWit().status();
            } else if (command.equals("checkout")) {
                if(printRequiredArgs(args.length, 2, "checkout", "branch_name/commit_sha")){
                    return;
                }

                WitVCS.getWit().checkout(args[1]);
            } else if (command.equals("branch")) {
                if(printRequiredArgs(args.length, 2, "branch", "branch_name")) {
                    return;
                }

                WitVCS.getWit().createBranch(args[1]);
            } else if (command.equals("rm-branch")) {

            } else if (command.equals("reset")) {

            } else if (command.equals("merge")) {

            } else {
                System.out.println("Invalid command.");
            }
        }
    }

    private static void init() {
        if(WitVCS.initialize()) {
            System.out.println("A wit version-control system already exists in this directory.");
        } else {
            if(WitVCS.getWit() == null) {
                System.out.println("Wit initialization failed.");
            } else {
                System.out.println("Wit repository initialized");
            }
        }
    }

    private static boolean printRequiredArgs(int n, int expected, String argName, String ... args) {
        if(n == expected) {
            return false;
        }

        System.out.print("Usage for wit " + argName + " :- java wit.Main " + argName);
        for(int i = 1; i < expected; i++) {
            System.out.print(" " + args[i-1]);
        }

        return true;
    }
}
