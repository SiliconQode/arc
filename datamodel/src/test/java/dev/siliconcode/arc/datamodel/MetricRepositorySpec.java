/**
 * The MIT License (MIT)
 *
 * Empirilytics Arc DataModel
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
package dev.siliconcode.arc.datamodel;

import org.javalite.activejdbc.test.DBSpec;
import org.junit.Test;

public class MetricRepositorySpec extends DBSpec {

    @Test
    public void shouldValidateRequiredAttributes() {
        MetricRepository repo = new MetricRepository();
        a(repo).shouldBe("valid");
//        //a(repo.errors().get("author")).shouldBeEqual("Author must be provided");
        repo.set("repoKey", "key", "name", "repo");
        a(repo).shouldBe("valid");
        repo.save();
        repo = (MetricRepository) MetricRepository.findAll().get(0);
        a(repo.getId()).shouldNotBeNull();
        a(repo.get("name")).shouldBeEqual("repo");
        a(repo.get("repoKey")).shouldBeEqual("key");
        a(MetricRepository.count()).shouldBeEqual(1);
    }

    @Test
    public void canAddMetric() {
        MetricRepository repo = MetricRepository.createIt("repoKey", "key", "name", "repo");
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");

        repo.add(metric);
        a(repo.getAll(Metric.class).size()).shouldBeEqual(1);
    }

    @Test
    public void canRemoveMetric() {
        MetricRepository repo = MetricRepository.createIt("repoKey", "key", "name", "repo");
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");

        repo.add(metric);
        a(repo.getAll(Metric.class).size()).shouldBeEqual(1);
        repo = (MetricRepository) MetricRepository.findAll().get(0);
        repo.removeMetric(metric);
        a(repo.getAll(Metric.class).size()).shouldBeEqual(0);
    }

    @Test
    public void deleteHandlesCorrectly() {
        MetricRepository repo = MetricRepository.createIt("repoKey", "key", "name", "repo");
        Metric metric = Metric.createIt("metricKey", "metric", "name", "metric", "description", "description");

        repo.add(metric);

        a(MetricRepository.count()).shouldBeEqual(1);
        a(Metric.count()).shouldBeEqual(1);
        repo.delete(true);
        a(MetricRepository.count()).shouldBeEqual(0);
        a(Metric.count()).shouldBeEqual(0);
    }
}
