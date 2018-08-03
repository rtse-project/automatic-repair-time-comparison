package server.helper;

import javax.xml.bind.annotation.adapters.HexBinaryAdapter;
import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA1 {
    /**
     * Read the file and calculate the SHA-1 checksum
     * Thank to: user2084795
     * https://stackoverflow.com/questions/6293713/java-how-to-create-sha-1-for-a-file
     *
     * @param file
     *            the file to read
     * @return the hex representation of the SHA-1 using uppercase chars
     * @throws FileNotFoundException
     *             if the file does not exist, is a directory rather than a
     *             regular file, or for some other reason cannot be opened for
     *             reading
     * @throws IOException
     *             if an I/O error occurs
     * @throws NoSuchAlgorithmException
     *             should never happen
     *
     */
    public static String calcate(File file) {

        MessageDigest sha1 = null;
        try {
            sha1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            //not the case
        }
        try (InputStream input = new FileInputStream(file)) {

            byte[] buffer = new byte[8192];
            int len = input.read(buffer);

            while (len != -1) {
                sha1.update(buffer, 0, len);
                len = input.read(buffer);
            }

            return new HexBinaryAdapter().marshal(sha1.digest());
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @see SHA1#calcate(File)
     * @param file
     *              path of the file
     * @return @see SHA1#calcate(File)
     * @throws FileNotFoundException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    public static String calcate(String file) {
        return calcate(new File(file));
    }
}
