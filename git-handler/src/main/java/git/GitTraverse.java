package git;

import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.ResetCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.errors.LockFailedException;
import org.eclipse.jgit.internal.storage.file.FileRepository;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;

import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class GitTraverse {

    static String connectionQuery = "jdbc:postgresql://brock.isys.uni-klu.ac.at:5432/giovanni?user=giovanni&password=giovanni";
    static String sqlInsert = "INSERT INTO public.\"CommitInfo\"(\"IDProject\", \"Hash\", \"Date\") VALUES ( ?, ?, ?)";
    static String sqlProjectID = "SELECT \"ID\" FROM public.\"Projects\" WHERE \"ProjectName\" = ?";

    String projectName;
    long IDProject = 0;
    File projectFolder;
    Repository repository;
    Git git;

    public GitTraverse(String projectName, File projectFolder) throws Exception {
        this.projectName = projectName;
        this.projectFolder = projectFolder;
        this.repository = new FileRepository(new File(this.projectFolder, ".git"));
        this.git = new Git(this.repository);
        setIDProject();
    }

    public GitTraverse(String projectName, String projectFolder) throws Exception {
        this(projectName, new File(projectFolder));
    }

    private static String formatDate(Date when) {
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        return df.format(when);
    }

    private static void resetRepo(String revision, File repoDir) throws IOException, GitAPIException {
        try {
            File gitFolder = new File(repoDir, ".git.git");
            Repository repository = FileRepositoryBuilder.create(gitFolder);
            Git git = new Git(repository);

            git.clean().setCleanDirectories(true).call();

            try {
                git.reset().setMode(ResetCommand.ResetType.HARD).call();
                git.checkout().setName(revision).call();
            } catch (JGitInternalException jgitex) {
                if (jgitex.getCause() instanceof LockFailedException) {
                    //try to remove the lock file
                    File lockFile = new File(gitFolder, "index.lock");
                    FileUtils.forceDelete(lockFile);
                    git.reset().setMode(ResetCommand.ResetType.HARD).call();
                    git.checkout().setName(revision).call();
                } else {
                    //unexpected
                    throw jgitex;
                }
            }

        } catch (Exception e) {
            System.out.println("Could not reset the repository, leaving it untouched and proceed: " + e.getMessage());
            System.out.println("Further info: " + e.getCause().getMessage());
        }
    }

    public static void main(String[] args) throws Exception {
        GitTraverse traverse = new GitTraverse(args[0], args[1]);
        traverse.storeProjectInformation();
    }

    private void setIDProject() throws Exception {
        Connection conn = DriverManager.getConnection(connectionQuery);
        try {
            PreparedStatement st = conn.prepareStatement(sqlProjectID);
            st.setString(1, this.projectName);
            ResultSet rs = st.executeQuery();
            int howMAny = 0;
            while (rs.next()) {
                howMAny++;
                this.IDProject = rs.getLong("ID");
                if (howMAny == 0)
                    throw new Exception("Project does not exists!");
                if (howMAny > 1)
                    throw new Exception("Check your database. More than one project found!");
            }
        } finally {
            conn.close();
        }

    }

    public void storeProjectInformation() throws Exception {
        // open DB connection
        Connection conn = DriverManager.getConnection(connectionQuery);

        try {
            Iterable<RevCommit> logs = git.log().all().call();
            for (RevCommit rev : logs) {
                String hash = rev.getName();
                long date = rev.getCommitterIdent().getWhen().getTime();

                PreparedStatement st = conn.prepareStatement(sqlInsert);
                st.setLong(1, this.IDProject);
                st.setString(2, hash);
                st.setLong(3, date);
                st.execute();
            }
        } finally {
            conn.close();
        }
    }

    public long getIDProject() throws Exception {
        if (this.IDProject == 0) {
            setIDProject();
        }
        return this.IDProject;
    }
}