package daikon.annotation;

import intermediateModel.interfaces.IASTHasStms;
import intermediateModel.interfaces.IASTMethod;
import intermediateModel.interfaces.IASTStm;
import intermediateModel.interfaces.IASTVar;
import intermediateModel.structure.*;
import intermediateModel.visitors.ApplyHeuristics;
import intermediateModelHelper.envirorment.Env;

import java.util.HashSet;
import java.util.Set;

public class TimeVarCollector extends ApplyHeuristics {

    WatchingPoints wp;
    public WatchingPoints getTimeVariables(ASTClass c){
        wp = new WatchingPoints();
        super.subscribe(intermediateModelHelper.heuristic.v2.MarkTime.class);
        super.subscribe(intermediateModelHelper.heuristic.v2.TimeInSignature.class);
        super.subscribe(intermediateModelHelper.heuristic.v2.AssignmentTimeVar.class);
        super.subscribe(intermediateModelHelper.heuristic.v2.BooleanExpression.class);
        super.subscribe(intermediateModelHelper.heuristic.v2.MinMaxSearch.class);
        super.subscribe(intermediateModelHelper.heuristic.v2.ReturnExpression.class);
        super.subscribe(intermediateModelHelper.heuristic.v2.AddTimeVarToTimeExpression.class);
        super.start(c);
        return wp;
    }


    private void analyze(int line, Env env, String className, String methodName){
        Set<IASTVar> tVars = computeVars(env);
        if(tVars.size() > 0){
            wp.addWatchingPoint(className, methodName, line, tVars);
        }
    }

    private Set<IASTVar> computeVars(Env env) {
        Set<IASTVar> out = new HashSet<>();
        for(IASTVar v : env.getAllVarList()){
            if(v.isTimeCritical())
                out.add(v);
        }
        return out;
    }

    private int getMaxLine(IASTHasStms l){
        int out = 0;
        for(IASTStm s : l.getStms()){
            if(s.getLineEnd() > out)
                out = s.getLineEnd();
        }
        if(out == 0){
            out = l.getLineEnd();
        }
        return out;
    }

    private int getMaxLine(ASTTry elm) {
        return getMaxLine(elm.getTryBranch());
    }

    @Override
    protected void postAnalyzeASTWhile(ASTWhile elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeASTTryResources(ASTTryResources elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeASTFinally(ASTTry.ASTFinallyBranch elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeASTTry(ASTTry elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }


    @Override
    protected void postAnalyzeASTCatch(ASTTry.ASTCatchBranch elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeASTSynchronized(ASTSynchronized elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeElseBranch(ASTIf.ASTElseStms elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeIfBranch(ASTIf.ASTIfStms elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeASTForEach(ASTForEach elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeASTFor(ASTFor elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeASTDoWhile(ASTDoWhile elm, Env env) {
        analyze(getMaxLine(elm), env, super.getLastClass(), super.getLastMethod());
    }

    @Override
    protected void postAnalyzeASTMethod(IASTMethod elm, Env env) {
        analyze(elm.getLineEnd(), env, super.getLastClass(), super.getLastMethod());
    }


}
