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
/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class Runner {

    static void main(String[] args)
    {
        long start = System.currentTimeMillis()
        String input
        String output
        int reps = 1
        if (args.length >= 2)
        {
            input = args[0]
            output = args[1]
        }
        else
        {
            input = "C:\\experiments\\DesignPatterns\\experiment3.csv"
            output = "C:\\experiments\\DesignPatterns\\quality"
        }

        ExperimentDesign design = DesignReader.read(input)
        List<DesignNode> nodes = design.getNodes()
        // System.out.println("Nodes Available: " + nodes.size())
        ForkJoinPool pool = new ForkJoinPool()
        ExperimentControl control = new ExperimentControl(nodes)
        DataHolder holder = pool.invoke(control)
        ResultsWriter.write(output, holder, reps)

        long end = System.currentTimeMillis()
        double total = (double) (end - start)
        total /= 1000
//        System.out.println(holder.getData().size())
        System.out.println("Total time: " + total + " seconds")
    }
}
