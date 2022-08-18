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

import lombok.Builder;
import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.BelongsTo;

import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@BelongsTo(parent = System.class, foreignKeyName = "system_id")
public class PatternChain extends Model {

    public PatternChain() {}

    @Builder(buildMethodName = "create")
    public PatternChain(String chainKey) {
        set("chainKey", chainKey);
        saveIt();
    }

    public String getChainKey() { return getString("chainKey"); }

    public void addInstance(PatternInstance inst) {
        if (inst != null)
            inst.setChainID(this.getId());
    }

    public void removeInstance(PatternInstance inst) {
        if (inst != null && inst.getChainID() != null && inst.getChainID().equals(getId()))
            inst.setChainID(null);
    }

    public List<PatternInstance> getInstances() {
        return PatternInstance.find("parent_chain_id = ?", this.getId());
    }

    public void updateKey() {
        System parent = parent(System.class);
        setString("chainKey", parent.getKey() + ":" + getChainKey());
        saveIt();
    }
}
