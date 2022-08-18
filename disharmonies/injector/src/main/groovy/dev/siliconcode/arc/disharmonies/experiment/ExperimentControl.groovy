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

import java.util.concurrent.ForkJoinPool
import java.util.concurrent.RecursiveTask

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class ExperimentControl extends RecursiveTask<DataHolder> {

    private List<DesignNode> nodes
    private DataHolder holder

    static void main(String[] args)
    {
        ExperimentDesign design = DesignReader.read(args[0])
        List<DesignNode> nodes = design.getNodes()
        ForkJoinPool pool = new ForkJoinPool()
        ExperimentControl control = new ExperimentControl(nodes)
        pool.invoke(control)
    }

    /**
     * @param nodes
     */
    ExperimentControl(List<DesignNode> nodes)
    {
        this.nodes = nodes
        this.holder = new DataHolder()
    }

    @Override
    protected DataHolder compute()
    {
//        List<ExperimentRunner> runners = new ArrayList<>()
//        for (DesignNode node : nodes)
//        {
//            runners.add(new ExperimentRunner(node))
//        }
//
//        invokeAll(runners)
//
//        for (ExperimentRunner runner : runners)
//        {
//            runner.join()
//        }
//
//        for (ExperimentRunner runner : runners)
//        {
//            try
//            {
//                holder.mergeInto(runner.get())
//            }
//            catch (InterruptedException | ExecutionException e)
//            {
//
//            }
//        }
//
//        return holder
        return null
    }

}
