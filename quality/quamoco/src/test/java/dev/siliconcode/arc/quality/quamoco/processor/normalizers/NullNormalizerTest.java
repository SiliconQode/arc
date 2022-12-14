/**
 * The MIT License (MIT)
 *
 * Empirilytics Quamoco Implementation
 * Copyright (c) 2015-2021 Empirilytics
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
package dev.siliconcode.arc.quality.quamoco.processor.normalizers;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import dev.siliconcode.arc.datamodel.File;
import dev.siliconcode.arc.quality.quamoco.graph.edge.MeasureToMeasureNumberEdge;
import dev.siliconcode.arc.quality.quamoco.graph.node.FileFinding;
import dev.siliconcode.arc.quality.quamoco.graph.node.Finding;
import dev.siliconcode.arc.quality.quamoco.model.NormalizationRange;
import org.javalite.activejdbc.test.DBSpec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Set;

/**
 * The class <code>NullNormalizerTest</code> contains tests for the class
 * <code>{@link NullNormalizer}</code>.
 *
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class NullNormalizerTest extends DBSpec {

    private NullNormalizer fixture;

    /**
     * Run the NullNormalizer(Edge,String,NormalizationRange) constructor test.
     */
    @Test
    public void testNullNormalizer_1() {
        final NormalizationRange range = NormalizationRange.CLASS;

        final NullNormalizer result = new NullNormalizer(
                new MeasureToMeasureNumberEdge("edge", null, null), "LOC", range);

        // add additional test code here
        Assert.assertNotNull(result);
        Assert.assertEquals("LOC", result.getMetric());
    }

    /**
     * Run the BigDecimal normalize(List<Finding>) method test.
     */
    @Test
    public void testNormalize_2() {
        final List<Finding> findings = Lists.newArrayList();
        findings.add(new FileFinding(File.builder().fileKey("path").create(), "issue", "issue"));
        final double result = fixture.normalize(findings);

        // add additional test code here
        Assert.assertEquals(0.0, result, 0.001);
    }

    /**
     * Run the BigDecimal normalize(List<Finding>) method test.
     */
    @Test
    public void testNormalize_3() {
        final List<Finding> findings = null;
        final double result = fixture.normalize(findings);

        // add additional test code here
        Assert.assertEquals(0.0, result, 0.001);
    }

    /**
     * Perform pre-test initialization.
     *
     * @throws Exception
     *             if the initialization fails for some reason
     */
    @Before
    public void setUp() throws Exception {
        fixture = new NullNormalizer(
                new MeasureToMeasureNumberEdge("edge", null, null), "LOC", NormalizationRange.CLASS);
    }
}
