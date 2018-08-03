package daikon.instrumentation.preprocess;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;


/**
 * Created by giovanni on 07/03/2017.
 */
public class ReadConfFile {

    File file;
    String separator = ";";
    InputStream stream;
    private static final Logger LOGGER = LogManager.getLogger();
    private WatchPoints watchPoints = WatchPoints.getInstance();

    boolean isStream = false;

    public ReadConfFile(String path){
        this(new File(path));
    }

    public ReadConfFile(File file) {
        this.file = file;
    }

    public ReadConfFile(InputStream stream){
        this.isStream = true;
        this.stream = stream;
    }

    public String getSeparator() {
        return separator;
    }

    public void setSeparator(String separator) {
        this.separator = separator;
    }

    public void start(){
        try {
            if (isStream) {
                start(new InputStreamReader(stream));
            } else {
                start(new FileReader(this.file));
            }
        } catch (Exception e){
            //System.err.println("Cannot open file or stream: " + this.file + " -- " + this.stream);
            LOGGER.error("Cannot open conf file: {}",  this.file);
        }
    }

    protected void start(InputStreamReader file){
        boolean notHeader = false;
        try (BufferedReader br = new BufferedReader(file)) {
            String line;
            while ((line = br.readLine()) != null) {
                // use comma as separator
                String[] row = line.split(separator);
                if(notHeader) {
                    handleRow(row);
                }
                else {
                    notHeader = true;
                    handleHeader(row);
                }
            }
        } catch (IOException e) {
            // e.printStackTrace();
            LOGGER.error("Cannot read the conf file: {}", this.file);
        }
    }

    private void handleRow(String[] row) {
        if(row.length != 4){
            return;
        }
        String className    = row[0];
        String methodName   = row[1];
        int line            = Integer.parseInt(row[2]);
        String varList      = row[3].substring(1, row[3].length()-1);
        String[] vars = varList.split(",");
        className = Utility.convertPkgNametoJava(className);
        watchPoints.addWatchingPoint(className, methodName, line, vars);
        LOGGER.debug("Watching: {}.{}@{}", className, methodName, line);

    }

    private void handleHeader(String[] row) {
    }


}

