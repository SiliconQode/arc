/**
 * MIT License
 *
 * Empirilytics PlantUML Editor
 * Copyright (c) 2018-2021 Empirilytics
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
package dev.siliconcode.arc.ui.plantuml.action.toolbar;

import dev.siliconcode.arc.ui.plantuml.PlantUMLEditor;
import dev.siliconcode.arc.ui.plantuml.action.AbstractFileAction;
import org.kordamp.ikonli.material.Material;
import org.kordamp.ikonli.swing.FontIcon;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Renders the UML for the currently selected tab
 *
 * @author Isaac Griffith
 * @version 1.1.1
 */
public class RenderAction extends AbstractFileAction {

    /**
     * Construct a new NewAction attached to the given PlantUMLEditor
     *
     * @param owner Owner of this Action
     */
    public RenderAction(PlantUMLEditor owner) {
        super(owner,
                "Render",
                "Renders UML in selected tab",
                FontIcon.of(Material.PLAY_CIRCLE_OUTLINE, 20, Color.BLACK),
                KeyEvent.VK_R,
                KeyStroke.getKeyStroke(KeyEvent.VK_F5, 0, true));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        owner.renderCurrentTab();
    }
}
