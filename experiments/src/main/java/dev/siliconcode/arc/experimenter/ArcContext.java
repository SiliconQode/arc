/**
 * The MIT License (MIT)
 *
 * Empirilytics Arc Framework
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
package dev.siliconcode.arc.experimenter;

import com.google.common.collect.Maps;
import dev.siliconcode.arc.datamodel.Project;
import dev.siliconcode.arc.datamodel.System;
import dev.siliconcode.arc.datamodel.util.DBCredentials;
import dev.siliconcode.arc.datamodel.util.DBManager;
import dev.siliconcode.arc.experimenter.db.DbProperties;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Properties;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
public class ArcContext {

    @Getter @Setter
    String language;

    @Getter @Setter
    Project project;

    @Getter @Setter
    System system;

    Map<String, Command> commandMap;
    Map<String, Collector> collectorMap;

    @Setter
    private Properties arcProperties;

    Logger logger;

    public ArcContext(Logger logger) {
        this.logger = logger;
        commandMap = Maps.newHashMap();
        collectorMap = Maps.newHashMap();
    }

    public String getArcProperty(String prop) {
        if (arcProperties.containsKey(prop)) {
            return arcProperties.getProperty(prop);
        }

        return null; // consider throwing an exception here
    }

    public void setArcProperty(String prop, String value) {
        if (prop != null && !prop.isBlank() && value != null && !prop.isBlank())
            arcProperties.setProperty(prop, value);
    }

    public void registerCommand(Command command) {
        if (command != null)
            commandMap.put(command.getToolName(), command);
    }

    public void registerCollector(Collector collector) {
        if (collector != null)
            collectorMap.put(collector.getName(), collector);
    }

    public Command getRegisteredCommand(String name) {
        return commandMap.get(name);
    }

    public Collector getRegisteredCollector(String name) {
        return collectorMap.get(name);
    }

    public void addArcProperty(String key, String value) {
        if (arcProperties == null) {
            arcProperties = new Properties();
        }
        arcProperties.setProperty(key, value);
    }

    public void updateProperty(String key, String newValue) {
        arcProperties.setProperty(key, newValue);
    }

    public String getProjectDirectory() {
        if (project != null) {
            String path = "";
            if (!DBManager.instance.isOpen())
            {
                open();
                path = project.getFullPath();
                close();
            } else
                path = project.getFullPath();

            return path;
        } else {
            return null;
        }
    }

    public void open() {
        DBManager.getInstance().open(getDBCreds());
    }

    public void close() {
        DBManager.getInstance().close();
    }

    public void createDatabase() {
        DBManager.getInstance().createDatabase(getDBCreds());
    }

    public String getReportDirectory() {
        return getArcProperty(ArcProperties.TOOL_OUTPUT_DIR);
    }

    public DBCredentials getDBCreds() {
        return DBCredentials.builder()
                .type(getArcProperty(DbProperties.DB_TYPE))
                .driver(getArcProperty(DbProperties.DB_DRIVER))
                .url(getArcProperty(DbProperties.DB_URL))
                .user(getArcProperty(DbProperties.DB_USER))
                .pass(getArcProperty(DbProperties.DB_PASS))
                .create();
    }
}
