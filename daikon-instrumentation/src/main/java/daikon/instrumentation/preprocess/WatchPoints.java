package daikon.instrumentation.preprocess;


import java.util.*;

public class WatchPoints {

    //Singleton
    private static WatchPoints instance = null;
    private WatchPoints(){}
    public static synchronized WatchPoints getInstance() {
        if(instance == null){
            instance = new WatchPoints();
        }
        return instance;
    }

    List<WatchPoint> points = new ArrayList<>();

    public void addWatchingPoint(String className, String methodName, int line, Set<String> variableName){
        points.add(new WatchPoint(className, methodName, line, variableName));
    }

    public void addWatchingPoint(String className, String methodName, int line, String[] variableName){
        Set<String> vars = new HashSet<>();
        for(String v : variableName){
            vars.add(v);
        }
        WatchPoint p = new WatchPoint(className, methodName, line, vars);
        if(!points.contains(p))
            points.add(p);
    }

    public int size(){
        return points.size();
    }

    public boolean shouldWatch(String className) {
        for(WatchPoint wp : points){
            if(wp.className.equals(className))
                return true;
        }
        return false;
    }

    public List<String> getMethodDefinitions(String className){
        List<String> out = new ArrayList<>();
        for(WatchPoint p : points){
            if(p.isTheOne(className))
                out.add(p.printAsMethodDef());
        }
        return out;
    }

    public List<WatchPoint> getWatchPoints(String className){
        List<WatchPoint> out = new ArrayList<>();
        for(WatchPoint p : points){
            if(p.isTheOne(className))
                out.add(p);
        }
        out.sort(new Comparator<WatchPoint>() {
            @Override
            public int compare(WatchPoint o1, WatchPoint o2) {
                return o1.line - o2.line;
            }
        });
        return out;
    }

}