/*
 * MIT License
 *
 * Empirilytics Base-Server
 * Copyright (c) 2020-2021 Empirilytics
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
package dev.siliconcode.arc.server

import dev.siliconcode.arc.server.config.ConfigLoader
import dev.siliconcode.arc.server.config.ModelContext
import dev.siliconcode.arc.datamodel.util.DBManager
import io.javalin.Javalin
import io.javalin.core.JavalinConfig

import java.util.function.Consumer

/**
 * Class containing the basic logic needed to setup and get a server running.
 * This does not contain any route information. Rather it leaves this to the
 * child classes. Note that this is an implementation of the Template Method
 * pattern.
 *
 * @author Isaac Griffith
 * @version 1.0.0
 */
abstract class BaseServer {

    protected Javalin app
    protected ModelContext context
    protected DBManager manager

    /**
     * Initializes the loads the configuration, sets up the database manager instance,
     * creates the server and calls to setup the routes.
     */
    final void init() {
        ConfigLoader loader = ConfigLoader.instance
        context = loader.load(new File("server.conf"))
        manager = DBManager.instance

        app = Javalin.create(config())

        routes()
    }

    /**
     * Start the server on the port specified in the configuration
     * or at port 7000 if the configuration is null or the port is
     * not specified or not a number
     */
    final void start() {
        int port
        try {
            port = Integer.parseInt(context.server.port)
        } catch (Exception ex) {
            port = 7000
        }

        manager.open(context)
        app.start(port)
    }

    /**
     * Stops the server
     */
    final void stop() {
        app.stop()
        manager.close()
    }

    /**
     * Hook method for setting up the routes for this server. This
     * method is called by init()
     */
    abstract void routes()

    /**
     * Hook Method which provides the JavalinConfig information to the server
     * the default returns an empty configuration
     * @return a Consumer of the JavalinConfig used in customizing the server
     */
    Consumer<JavalinConfig> config() {
        return {}
    }
}
