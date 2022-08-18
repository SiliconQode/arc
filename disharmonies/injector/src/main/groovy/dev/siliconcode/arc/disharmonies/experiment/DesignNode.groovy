/*
 * The MIT License (MIT)
 *
 * Empirilytics Disharmony Injector
 * Copyright (c) 2017-2021 Empirilytics
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package dev.siliconcode.arc.disharmonies.experiment

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DesignNode {
    /**
     * @return the patternType
     */
    String getPatternType()
    {
        return patternType
    }

    /**
     * @param patternType
     *            the patternType to set
     */
    void setPatternType(String patternType)
    {
        this.patternType = patternType
    }

    /**
     * @return the instanceLoc
     */
    String getInstanceLoc()
    {
        return instanceLoc
    }

    /**
     * @param instanceLoc
     *            the instanceLoc to set
     */
    void setInstanceLoc(String instanceLoc)
    {
        this.instanceLoc = instanceLoc
    }

    /**
     * @return the injectType
     */
    String getInjectType()
    {
        return injectType
    }

    /**
     * @param injectType
     *            the injectType to set
     */
    void setInjectType(String injectType)
    {
        this.injectType = injectType
    }

    /**
     * @return the system
     */
    String getSystem()
    {
        return system
    }

    /**
     * @param system
     *            the system to set
     */
    void setSystem(String system)
    {
        this.system = system
    }

    void setRep(int rep)
    {
        this.rep = rep
    }

    int getRep()
    {
        return rep
    }

    private String patternType
    private String instanceLoc
    private String injectType
    private String system
    private int rep

    DesignNode(String type, int rep, String loc, String inject, String system) throws DesignNodeException
    {
        this.patternType = type
        this.rep = rep
        this.instanceLoc = loc
        this.injectType = inject
        this.system = system

        verify()
    }

    void verify() throws DesignNodeException
    {
        Path path = Paths.get(instanceLoc)
        if (!Files.exists(path) || !Files.isDirectory(path))
            throw new DesignNodeException("The location specified: " + instanceLoc
                    + " either does not exist or is not a directory.")

        if (!isAcceptableInjectType(injectType))
            throw new DesignNodeException("The injection type specified: " + injectType
                    + " is not a known acceptable injection type.")
    }

    /**
     * @param injectType
     * @return
     */
    private boolean isAcceptableInjectType(String injectType)
    {
//        switch (injectType)
//        {
//            case GrimeInjector.DEPG:
//            case GrimeInjector.DIPG:
//            case GrimeInjector.DESG:
//            case GrimeInjector.DISG:
//            case GrimeInjector.IEPG:
//            case GrimeInjector.IIPG:
//            case GrimeInjector.IESG:
//            case GrimeInjector.IISG:
//                return true
//        }

        return false
    }
}
