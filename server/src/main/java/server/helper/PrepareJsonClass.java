package server.helper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import intermediateModel.interfaces.IASTMethod;
import intermediateModel.interfaces.IASTStm;
import intermediateModel.structure.ASTClass;
import intermediateModel.structure.ASTHiddenClass;
import intermediateModel.structure.ASTRE;
import intermediateModel.structure.expression.ASTAssignment;
import intermediateModel.structure.expression.ASTVariableDeclaration;
import intermediateModel.types.TimeTypeSystem;
import intermediateModel.visitors.ApplyHeuristics;
import intermediateModel.visitors.DefaultASTVisitor;
import intermediateModelHelper.envirorment.Env;
import intermediateModelHelper.envirorment.temporal.structure.Constraint;
import server.handler.getFile;
import server.handler.middleware.ParsePars;

import java.util.List;
import java.util.Map;

public class PrepareJsonClass {
    public static String json(List<ASTClass> classes, Map<String, String> parameters, String file_path, String basepath) {
        //annotate with env and time
        for(ASTClass c : classes){
            AnnotateEnv a = new AnnotateEnv();
            a.start(c);
            ApplyHeuristics ah = new ApplyHeuristics();
            ah.subscribe(intermediateModelHelper.heuristic.v2.MarkTime.class);
            ah.subscribe(intermediateModelHelper.heuristic.v2.TimeInSignature.class);
            ah.subscribe(intermediateModelHelper.heuristic.v2.AssignmentTimeVar.class);
            ah.subscribe(intermediateModelHelper.heuristic.v2.BooleanExpression.class);
            ah.subscribe(intermediateModelHelper.heuristic.v2.MinMaxSearch.class);
            ah.subscribe(intermediateModelHelper.heuristic.v2.ReturnExpression.class);
            ah.subscribe(intermediateModelHelper.heuristic.v2.AddTimeVarToTimeExpression.class);

            ah.analyze(c);
            //annotate each method
            for(IASTMethod m : c.getMethods()){
                //m.setDeclaredVars();
                m.visit(new RemoveCnt());
            }
            for(Constraint cnst : ah.getTimeConstraint()){
                cnst.removeElm();
            }
            if(!basepath.endsWith("/")){
                basepath = basepath + "/";
            }
            c.setPath(file_path.replace(basepath,""));
            c.setVersion(PropertiesFileReader.getInfo());
        }
        //annotate with Time


        // send response
        ObjectMapper json = ParsePars.getOutputFormat(parameters);
        json.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            return json.writeValueAsString(classes);
        } catch (JsonProcessingException e) {
            return "";
        }
    }

    static class AnnotateEnv extends TimeTypeSystem {
        @Override
        public void start(ASTClass c) {
            super.start(c);
            c.setParent(null);
            c.removeChild();
        }

        @Override
        protected void analyzeASTRE(ASTRE r, Env env) {
            super.analyzeASTRE(r, env);
            r.setEnv(env);
        }
    }

    static class RemoveCnt extends DefaultASTVisitor {
        public RemoveCnt() {
            super.setExcludeHiddenClass(false);
        }

        @Override
        public void enterSTM(IASTStm s) {
            s.removeCnstr();
        }

        @Override
        public void enterASTVariableDeclaration(ASTVariableDeclaration elm) {
            elm.removeCnstr();
        }

        @Override
        public void enterASTAssignment(ASTAssignment elm) {
            elm.removeCnstr();
        }

        @Override
        public void enterASTHiddenClass(ASTHiddenClass astHiddenClass) {
            astHiddenClass.setParent(null);
        }
    }
}
