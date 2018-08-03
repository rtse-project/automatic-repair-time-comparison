package server.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * Created by giovanni on 12/05/2017.
 */
public class PropertiesFileReader {

    private static final Properties properties;

    /** Use a static initializer to read from file. */
    static {
        InputStream inputStream = PropertiesFileReader.class.getResourceAsStream("/buildNumber.properties");
        properties = new Properties();
        try {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read properties file", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // Ignore
                }
            }
        }
    }

    /** Hide default constructor. */
    private PropertiesFileReader() {}

    /**
     * Gets the Git SHA-1.
     * @return A {@code String} with the Git SHA-1.
     */
    public static String getGitSha1() {
        String v = properties.getProperty("git-sha-1");
        return ( (v != null && !v.equals("")) ? v : getShaFallback());
    }

    private static String getShaFallback(){
        return getGitInfo("git rev-parse HEAD");
    }
    private static String getBranchFallback(){
        return getGitInfo("git rev-parse --abbrev-ref HEAD");
    }

    private static String getGitInfo(String cmd){
        Runtime rt = Runtime.getRuntime();
        StringBuilder sb = new StringBuilder();
        try {
            Process pr = rt.exec(cmd);
            BufferedReader stdInput = new BufferedReader(new
                    InputStreamReader(pr.getInputStream()));
            pr.waitFor();
            String s = null;
            while ((s = stdInput.readLine()) != null) {
                sb.append(s);
            }
        } catch (Exception e) {
            //
        }
        return sb.toString();
    }

    /**
     * Gets the Git Branch Name.
     * @return A {@code String} with the Git Branch Name.
     */
    public static String getBranch() {
        String v = properties.getProperty("branch");
        return ( (v != null && !v.equals("")) ? v : getBranchFallback());
    }

    public static String getInfo(){
        return String.format("Branch: %s -- Commit: %s", getBranch(), getGitSha1());
    }
}
