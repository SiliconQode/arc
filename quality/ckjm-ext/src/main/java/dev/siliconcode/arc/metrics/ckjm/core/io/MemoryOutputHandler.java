package dev.siliconcode.arc.metrics.ckjm.core.io;

import dev.siliconcode.arc.metrics.ckjm.core.ClassMetrics;

import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.Maps;

/**
 * Output handler, that stores the metrics in memory.
 * @author marian
 * @version 2.5.0
 */
public class MemoryOutputHandler implements CkjmOutputHandler{

    private Map<String, ClassMetrics> mResults = Maps.newHashMap();

    public void handleClass(String name, ClassMetrics c) {
        mResults.put( name, c );
    }

    public ClassMetrics getMetrics( String className ){
        return mResults.get(className);
    }

    public void close() {}
}
