package com.jitlogic.zorka.spy;

import com.jitlogic.zorka.bootstrap.AgentMain;
import com.jitlogic.zorka.mbeans.AttrGetter;
import com.jitlogic.zorka.mbeans.ValGetter;
import com.jitlogic.zorka.util.ObjectInspector;
import com.jitlogic.zorka.util.ZorkaLog;
import com.jitlogic.zorka.util.ZorkaLogger;
import org.objectweb.asm.MethodVisitor;

/**
 * @author RLE <rafal.lewczuk@gmail.com>
 */
public class PresentingDataCollector implements DataCollector {

    private final ZorkaLog log = ZorkaLogger.getLog(this.getClass());

    private long id;
    private String mbsName, mbeanName, attrName;
    private Object argObj;
    private String[] argPath, getPath;
    private int type;
    private boolean once;
    private volatile boolean gotIt = false;

    private ObjectInspector inspector = new ObjectInspector();


    public PresentingDataCollector(String mbsName, String mbeanName, String attrName,
                Object argObj, String[] argPath, String[] getPath, int type, boolean once) {
        this.mbsName = mbsName;
        this.mbeanName = mbeanName;
        this.attrName = attrName;
        this.argObj = argObj;
        this.argPath = argPath;
        this.getPath = getPath;
        this.type = type;
        this.once = once;
        this.id = MainCollector.register(this);
    }


    public CallInfo logStart(long id, long tst, Object[] args) {

        if (once && gotIt) return null;

        Object obj = args[0];

        for (Object o : argPath) {
            obj = inspector.get(args[0], o);
        }

        log.debug("Presenting object as attribute '" + attrName + " at '" + mbeanName + "'");

        if (getPath != null && getPath.length > 0) {
            ValGetter getter = new AttrGetter(obj, getPath);
            AgentMain.getAgent().registerBeanAttr(mbsName, mbeanName, attrName, getter);
        } else {
            AgentMain.getAgent().registerBeanAttr(mbsName, mbeanName, attrName, obj);
        }

        return null;
    }


    public void logCall(long tst, CallInfo info) {
    }


    public void logError(long tst, CallInfo info) {
    }


    public MethodVisitor getAdapter(MethodVisitor mv) {
        if (argObj instanceof String) {
            return new SimpleMethodInstrumentator(mv, id, (String)argObj);
        } else {
            return new SimpleMethodInstrumentator(mv, id, (Integer)argObj);
        }
    }

}