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

import com.google.common.collect.Maps
import org.apache.commons.lang3.tuple.Pair

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class DataHolder {

    private Map<String, Map<Integer, Map<String, Map<String, Pair<Double, Double>>>>> data

    DataHolder()
    {
        data = Maps.newConcurrentMap()
    }

    void addBefore(int rep, String pattern, String inject, String quality, double value)
    {
//        if (data.containsKey(pattern))
//        {
//            Map<Integer, Map<String, Map<String, Pair<Double, Double>>>> reps = data.get(pattern)
//            if (reps.containsKey(rep))
//            {
//                Map<String, Map<String, Pair<Double, Double>>> injects = reps.get(rep)
//                if (injects.containsKey(inject))
//                {
//                    Map<String, Pair<Double, Double>> qualVals = injects.get(inject)
//                    if (qualVals.containsKey(quality))
//                    {
//                        Pair<Double, Double> pair = qualVals.get(quality)
//                        pair.setFirst(value)
//                    }
//                    else
//                    {
//                        Pair<Double, Double> values = new Pair<>()
//                        values.setFirst(value)
//                        qualVals.put(quality, values)
//                    }
//                }
//                else
//                {
//                    Map<String, Pair<Double, Double>> qualVals = Maps.newConcurrentMap()
//                    Pair<Double, Double> pair = new Pair<>()
//                    pair.setFirst(value)
//                    qualVals.put(quality, pair)
//                    injects.put(inject, qualVals)
//                }
//            }
//            else
//            {
//                Map<String, Map<String, Pair<Double, Double>>> injects = Maps.newConcurrentMap()
//                Map<String, Pair<Double, Double>> qualVals = Maps.newConcurrentMap()
//                Pair<Double, Double> pair = new Pair<>()
//                pair.setFirst(value)
//                qualVals.put(quality, pair)
//                injects.put(inject, qualVals)
//                reps.put(rep, injects)
//            }
//        }
//        else
//        {
//            Map<Integer, Map<String, Map<String, Pair<Double, Double>>>> reps = Maps.newConcurrentMap()
//            Map<String, Map<String, Pair<Double, Double>>> injects = Maps.newConcurrentMap()
//            Map<String, Pair<Double, Double>> qualVals = Maps.newConcurrentMap()
//            Pair<Double, Double> pair = new Pair<>()
//            pair.setFirst(value)
//            qualVals.put(quality, pair)
//            injects.put(inject, qualVals)
//            reps.put(rep, injects)
//            data.put(pattern, reps)
//        }
    }

    void addAfter(int rep, String pattern, String inject, String quality, double value)
    {
//        if (data.containsKey(pattern))
//        {
//            Map<Integer, Map<String, Map<String, Pair<Double, Double>>>> reps = data.get(pattern)
//            if (reps.containsKey(rep))
//            {
//                Map<String, Map<String, Pair<Double, Double>>> injects = reps.get(rep)
//                if (injects.containsKey(inject))
//                {
//                    Map<String, Pair<Double, Double>> qualVals = injects.get(inject)
//                    if (qualVals.containsKey(quality))
//                    {
//                        Pair<Double, Double> pair = qualVals.get(quality)
//                        pair.setLast(value)
//                    }
//                    else
//                    {
//                        Pair<Double, Double> values = new Pair<>()
//                        values.setLast(value)
//                        qualVals.put(quality, values)
//                    }
//                }
//                else
//                {
//                    Map<String, Pair<Double, Double>> qualVals = Maps.newConcurrentMap()
//                    Pair<Double, Double> pair = new Pair<>()
//                    pair.setLast(value)
//                    qualVals.put(quality, pair)
//                    injects.put(inject, qualVals)
//                }
//            }
//            else
//            {
//                Map<String, Map<String, Pair<Double, Double>>> injects = Maps.newConcurrentMap()
//                Map<String, Pair<Double, Double>> qualVals = Maps.newConcurrentMap()
//                Pair<Double, Double> pair = new Pair<>()
//                pair.setLast(value)
//                qualVals.put(quality, pair)
//                injects.put(inject, qualVals)
//                reps.put(rep, injects)
//            }
//        }
//        else
//        {
//            Map<Integer, Map<String, Map<String, Pair<Double, Double>>>> reps = Maps.newConcurrentMap()
//            Map<String, Map<String, Pair<Double, Double>>> injects = Maps.newConcurrentMap()
//            Map<String, Pair<Double, Double>> qualVals = Maps.newConcurrentMap()
//            Pair<Double, Double> pair = new Pair<>()
//            pair.setLast(value)
//            qualVals.put(quality, pair)
//            injects.put(inject, qualVals)
//            reps.put(rep, injects)
//            data.put(pattern, reps)
//        }
    }

    void mergeInto(DataHolder holder)
    {
        Map<String, Map<Integer, Map<String, Map<String, Pair<Double, Double>>>>> otherData = holder.getData()
        for (String pattern : otherData.keySet())
        {
            if (data.containsKey(pattern))
            {
                for (int rep : otherData.get(pattern).keySet())
                {
                    if (data.get(pattern).containsKey(rep))
                    {
                        for (String inject : otherData.get(pattern).get(rep).keySet())
                        {
                            if (data.get(pattern).get(rep).containsKey(inject))
                            {
                                for (String quality : otherData.get(pattern).get(rep).get(inject).keySet())
                                {
                                    data.get(pattern).get(rep).get(inject)
                                            .put(quality, otherData.get(pattern).get(rep).get(inject).get(quality))
                                }
                            }
                            else
                            {
                                Map<String, Pair<Double, Double>> map = Maps.newConcurrentMap()
                                for (String quality : otherData.get(pattern).get(rep).get(inject).keySet())
                                {
                                    map.put(quality, otherData.get(pattern).get(rep).get(inject).get(quality))
                                }
                                data.get(pattern).get(rep).put(inject, map)
                            }
                        }
                    }
                    else
                    {
                        Map<String, Map<String, Pair<Double, Double>>> mainMap = Maps.newConcurrentMap()
                        for (String inject : otherData.get(pattern).get(rep).keySet())
                        {
                            Map<String, Pair<Double, Double>> map = Maps.newConcurrentMap()
                            for (String quality : otherData.get(pattern).get(rep).get(inject).keySet())
                            {
                                map.put(quality, otherData.get(pattern).get(rep).get(inject).get(quality))
                            }
                            mainMap.put(inject, map)
                        }
                        data.get(pattern).put(rep, mainMap)
                    }
                }
            }
            else
            {
                Map<Integer, Map<String, Map<String, Pair<Double, Double>>>> mainMap = Maps.newConcurrentMap()
                for (int rep : otherData.get(pattern).keySet())
                {
                    Map<String, Map<String, Pair<Double, Double>>> repMap = Maps.newConcurrentMap()
                    for (String inject : otherData.get(pattern).get(rep).keySet())
                    {
                        Map<String, Pair<Double, Double>> map = Maps.newConcurrentMap()
                        for (String quality : otherData.get(pattern).get(rep).get(inject).keySet())
                        {
                            map.put(quality, otherData.get(pattern).get(rep).get(inject).get(quality))
                        }
                        repMap.put(inject, map)
                    }
                    mainMap.put(rep, repMap)
                }
                data.put(pattern, mainMap)
            }
        }
    }

    /**
     * @return
     */
    Map<String, Map<Integer, Map<String, Map<String, Pair<Double, Double>>>>> getData()
    {
        return data
    }

    String getDataLine(String pattern, int rep, String inject, String quality)
    {
        String retVal = null
        if (data.containsKey(pattern))
        {
            System.out.println("Data Contains Pattern")
            Map<Integer, Map<String, Map<String, Pair<Double, Double>>>> reps = data.get(pattern)
            if (reps.containsKey(rep))
            {
                System.out.println("Data contains rep")
                Map<String, Map<String, Pair<Double, Double>>> injects = reps.get(rep)
                if (injects.containsKey(inject))
                {
                    System.out.println("Data contains Inject")
                    Map<String, Pair<Double, Double>> qualVals = injects.get(inject)
                    if (qualVals.containsKey(quality))
                    {
                        System.out.println("Data contains quality")
                        Pair<Double, Double> pair = qualVals.get(quality)
                        System.out.println(pair)
                        double diff = pair.getLast() - pair.getFirst()

                        retVal = String.format("%s,%s,%f", pattern.replaceAll(/ /, "-"), inject, diff)
                    }
                }
            }
        }

        return retVal
    }

    /**
     * @return
     */
    Set<String> getKeys()
    {
        return this.data.keySet()
    }
}
