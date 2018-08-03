package server.cmd;

import com.sun.net.httpserver.HttpExchange;
import server.Config;
import server.helper.MockHTTPEntry;
import server.helper.PropertiesFileReader;

import java.io.IOException;

public class ArgumentParser {
    private ArgumentParser(){}

    private static String openProject = "openProject";
    private static String isPrjOpen = "isProjectOpen";
    private static String getFile = "getFile";
    private static String getAllFiles = "getAllFiles";
    private static String getStatus = "getStatus";
    private static String clean = "clean";
    private static String cleanAll = "cleanAll";


    private String name = "";
    private String path = "";
    private String format = "json";
    private String cmd = "";
    private boolean debug = false;

    static {
        intermediateModelHelper.Config.setDebug(false);
    }

    public static ArgumentParser parse(String[] args){
        if(args.length == 0){
            printHelp();
        }
        ArgumentParser ret = new ArgumentParser();
        int i = 0;
        while(i < args.length){
            String actual = args[i];
            switch (actual){
                case "-h": printHelp(); break;
                case "-cmd": {
                    i++;
                    ret.cmd = args[i];
                } break;
                case "-debug": {
                    ret.setDebug(true);
                } break;
                case "-name": {
                    i++;
                    ret.name = args[i];
                } break;
                case "-path": {
                    i++;
                    ret.path = args[i];
                } break;
                case "-format": {
                    i++;
                    ret.format = args[i];
                } break;
                default: printHelp(); break;
            }
            i++;
        }
        ret.checks();
        return ret;
    }

    private void setDebug(boolean b) {
        this.debug = b;
        Config.setDebug(b);
    }

    private void checks() {
       if(cmd.equals(openProject)){
           //do checks
           //smth wrong. error message and then help
       }
       //and so fort
    }

    private static void printHelp(){
        System.out.println("Version : " + PropertiesFileReader.getInfo());
        System.out.println("The following commands are supported");
        System.out.println("-debug\tPrint debug info");
        System.out.println("-h\tPrint this message and exit");
        System.out.println("-format [format] sets the output format");
        System.out.println("\tjson");
        System.out.println("\tyaml");
        System.out.println("-cmd [name]\t execute the [name] command");
        System.out.println(String.format("\t%s opens the project", openProject));
        System.out.println(String.format("\t%s verifies that the project is opened", isPrjOpen));
        System.out.println(String.format("\t%s get a file", getFile));
        System.out.println(String.format("\t%s get the list of java files of the project", getAllFiles));
        System.out.println(String.format("\t%s returns the status", getStatus));
        System.out.println(String.format("\t%s clean a project", clean));
        System.out.println(String.format("\t%s clean everything", cleanAll));
        System.out.println("------------------");
        System.out.println("Extra parameters to make -cmd [name] works correctly");
        System.out.println("\t-name [name] set the project name. Mandatory for:");
        System.out.println("\t\t" + openProject);
        System.out.println("\t\t" + isPrjOpen);
        System.out.println("\t\t" + getFile);
        System.out.println("\t\t" + getAllFiles);
        System.out.println("\t\t" + getStatus);
        System.out.println("\t\t" + clean);
        System.out.println("\t-path file://[path] set a file path. Mandatory for:");
        System.out.println("\t\t" + openProject);
        System.out.println("\t\t" + getFile);
        System.exit(0);
    }

    public void call() throws IOException {
        MockHTTPEntry m = new MockHTTPEntry(this);

        HttpExchange he = m.getMock();
        server.handler.openProject op = m.getOPMock();

        if(cmd.equals(getFile)){
            server.handler.getFile gf = new server.handler.getFile();
            gf.handle(he);
        }
        if(cmd.equals(openProject)){
            server.handler.openProjectCMD gf = new server.handler.openProjectCMD();
            gf.handle(he);
        }
        if(cmd.equals(isPrjOpen)){
            server.handler.isProjectOpen gf = new server.handler.isProjectOpen();
            gf.handle(he);
        }
        if(cmd.equals(getAllFiles)){
            server.handler.getAllFiles gf = new server.handler.getAllFiles();
            gf.handle(he);
        }
        if(cmd.equals(getStatus)){
            server.handler.getStatus gf = new server.handler.getStatus(op);
            gf.handle(he);
        }
        if(cmd.equals(clean)){
            server.handler.clean gf = new server.handler.clean(op);
            gf.handle(he);
        }
        if(cmd.equals(cleanAll)){
            server.handler.cleanAll gf = new server.handler.cleanAll(op);
            gf.handle(he);
        }

    }


    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public String getFormat() {
        return format;
    }
}
