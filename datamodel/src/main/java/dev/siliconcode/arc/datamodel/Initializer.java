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

import dev.siliconcode.arc.datamodel.cfg.ControlFlowGraph;
import lombok.Builder;
import org.javalite.activejdbc.annotations.BelongsTo;
import org.javalite.activejdbc.annotations.BelongsToPolymorphic;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class Initializer extends Member {

    public Initializer() {}


    @Builder(buildMethodName = "create")
    public Initializer(String name, int start, int end, String compKey, Accessibility accessibility, boolean instance) {
        set("name", name, "start", start, "end", end, "compKey", compKey, "instance", instance);
        set("localVars", 0, "returnStmts", 1, "numStmts", 0, "numDecisionPoints", 0);
        if (accessibility != null)
            setAccessibility(accessibility);
        else
            setAccessibility(Accessibility.PUBLIC);
        save();
    }

    public boolean isInstance() {
        return getBoolean("instance");
    }

    public void setInstance(boolean instance) {
        setBoolean("instance", instance);
        save();
    }

    @Override
    public Member copy(String oldPrefix, String newPrefix) {
        Initializer copy = Initializer.builder()
                .name(this.getName())
                .compKey(this.getCompKey().replace(oldPrefix, newPrefix))
                .accessibility(this.getAccessibility())
                .instance(this.isInstance())
                .start(this.getStart())
                .end(this.getEnd())
                .create();

        //copy.setLocalVarCount(getLocalVarCount());
        getModifiers().forEach(copy::addModifier);

        return copy;
    }

    public void setLocalVarCount(int count) {
        setInteger("localVars", count);
    }

    public void incrementLocalVarCount() {
        setInteger("localVars", getInteger("localVars") + 1);
        save();
    }

    public int getLocalVarCount() {
        return getInteger("localVars");
    }

    public void incrementReturnStmts() {
        setInteger("returnStmts", getInteger("returnStmts") + 1);
        save();
    }

    public int getReturnStmts() {
        return getInteger("returnStmts");
    }

    public void incrementNumberOfStmts() {
        setInteger("numStmts", getInteger("numStmts") + 1);
        save();
    }

    public int getNumStmts() {
        return getInteger("numStmts");
    }

    public void incrementNumDecisionPoints() {
        setInteger("numDecisionPoints", getInteger("numDecisionPoints") + 1);
        save();
    }

    public int getNumDecisionPoints() {
        return getInteger("numDecisionPoints");
    }

    public void setCounts(int varCount, int returnCount, int stmtCount, int decisionCount) {
        set("localVars", varCount, "returnStmts", returnCount, "numStmts", stmtCount, "numDecisionPoints", decisionCount);
        save();
    }

    public ControlFlowGraph getCfg() {
        return ControlFlowGraph.fromString(getString("cfg"));
    }

    public void setCfg(ControlFlowGraph cfg) {
        set("cfg", cfg.cfgToString());
        save();
    }

    public Reference createReference() {
        return Reference.builder().refKey(getCompKey()).refType(RefType.INITIALIZER).create();
    }
}
