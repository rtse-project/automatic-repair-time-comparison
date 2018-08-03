package sql;




import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLManager {
    static String connectionQuery = "jdbc:postgresql://brock.isys.uni-klu.ac.at:5432/giovanni?user=giovanni&password=giovanni";
    Connection conn;
    public SQLManager() throws SQLException {
        conn = DriverManager.getConnection(connectionQuery);
    }

    public void close() throws SQLException {
        try {
            conn.close();
        } catch (SQLException e) {
            conn.close();
        }
    }


    public void addCommitInfo(String hash, String branch, long time, String projectName) throws SQLException {
        String sqlInsert = "INSERT INTO public.\"CommitInfo\"(\"Hash\", \"Branch\", \"Date\", \"ProjectName\") VALUES ( ?, ?, ?, ?)";
        PreparedStatement st = conn.prepareStatement(sqlInsert);
        st.setString(1, hash);
        st.setString(2, branch);
        st.setLong(  3, time);
        st.setString(4, projectName);
        st.execute();
    }

    public void updateCommitDate(String hash, long date) throws SQLException{
        String sqlUpdate = "UPDATE public.\"CommitInfo\" SET \"Date\"=? WHERE \"Hash\" = ?";
        PreparedStatement st = conn.prepareStatement(sqlUpdate);
        st.setLong(1, date);
        st.setString(2, hash);
        st.execute();
    }

    public void addCommitResult(String hash, long timeInit, long timeProcess, int warn, int err, String warning, String errors, String projectName) throws SQLException {
        String sqlInsert = "INSERT INTO public.\"CommitResult\"(\"Hash\", \"TimeInit\", \"TimeProcess\", \"WARN\", \"ERR\"," +
                "\"Warnings\", \"Errors\", \"ProjectName\") VALUES ( ?, ?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement st = conn.prepareStatement(sqlInsert);
        st.setString(1, hash);
        st.setLong(2, timeInit);
        st.setLong(3, timeProcess);
        st.setLong(4, warn);
        st.setLong(5, err);
        st.setString(6, warning);
        st.setString(7, errors);
        st.setString(8, projectName);
        st.execute();
    }
}
