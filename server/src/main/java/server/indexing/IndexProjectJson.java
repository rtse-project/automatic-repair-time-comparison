package server.indexing;

import intermediateModel.interfaces.IASTMethod;
import intermediateModel.structure.ASTClass;
import intermediateModel.structure.ASTMethod;
import intermediateModel.visitors.DefaultASTVisitor;
import intermediateModel.visitors.creation.JDTVisitor;
import server.helper.PrepareJsonClass;
import server.helper.SHA1;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import static intermediateModelHelper.indexing.IndexingProject.getJavaFiles;

public class IndexProjectJson {
    protected MongoConnectorServer db;
    protected String projectName;

    public IndexProjectJson(String name) {
        this.db = MongoConnectorServer.getInstance(name);
        this.projectName = name;
    }

    public void indexProject(String base_path, boolean deleteOld){
        //remove old preprocess
        db.setIndexStart();
        if(deleteOld) delete();
        Iterator i = getJavaFiles(base_path);
        int n_file = 0;
        while (i.hasNext()) {
            String filename = ((File)i.next()).getAbsolutePath();
            List<ASTClass> result = JDTVisitor.parse(filename, base_path);
            index(result, filename, base_path);
        }
        //ensure indexes
        db.ensureIndexes();
        db.setIndexFinish();
        db.setBasePath(base_path);
    }

    private void index(List<ASTClass> result, String path, String base_path) {
        final boolean[] hasMain = {false};
        final boolean[] hasThread = {false};
        final String[] klass = { result.size() > 0 ? result.get(0).getName() : "" };
        final String[] pkg = { result.size() > 0 ? result.get(0).getPackageName() : "" };
        List<String> extended = new ArrayList<>();
        for(ASTClass c : result){
            c.visit(new DefaultASTVisitor(){
                @Override
                public void enterASTMethod(ASTMethod elm) {
                    if(
                            elm.isStatic() &&
                            elm.getAccessModifier() == IASTMethod.AccessModifier.PUBLIC &&
                            elm.getName().equals("main")
                            ) {
                        hasMain[0] = true;
                        klass[0] = c.getName();
                        pkg[0] = c.getPackageName();
                    }
                }
            });
            if(c.getExtendClass().equals("Thread") || c.getImplmentsInterfaces().contains("Runnable")){
                hasThread[0] = true;
            }
            extended.add(c.getExtendClass());
            extended.addAll(c.getImplmentsInterfaces());
        }
        DBDataJSON j = new DBDataJSON();
        j.setPath(path);
        j.setKlassName(klass[0]);
        j.setPackageName(pkg[0]);
        j.setExtend(extended);
        j.setHasMain(hasMain[0] ? "true" : "false");
        j.setHasThread(hasThread[0] ? "true" : "false");
        j.setJson(PrepareJsonClass.json(result, new HashMap<>(), path, base_path));
        j.setSha1(SHA1.calcate(path));
        db.add(j);
    }

    public void delete(){
        db.drop();
    }
}
