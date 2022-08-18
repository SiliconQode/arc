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
package dev.siliconcode.arc.experimenter.impl.git;

import dev.siliconcode.arc.datamodel.Project;
import dev.siliconcode.arc.datamodel.SCMType;
import dev.siliconcode.arc.experimenter.ArcContext;
import dev.siliconcode.arc.experimenter.command.RepositoryCommand;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class GitCommand extends RepositoryCommand {

    @Setter
    @Getter
    protected Project project;
    @Setter
    @Getter
    protected String repoDir;
    @Setter
    @Getter
    protected String username;
    @Setter
    @Getter
    protected String password;
    @Setter
    protected String tag;
    protected ArcContext context;

    public GitCommand() {
        super("Git");
    }

    @Override
    public void execute(ArcContext context) {
        log.info("Running Git to retrieve the project");
        this.context = context;
        if (context != null) {
            this.project = context.getProject();
            this.repoDir = context.getProject().getFullPath();
            this.username = context.getArcProperty(GitProperties.GIT_USERNAME);
            this.password = context.getArcProperty(GitProperties.GIT_PASSWORD);
        }

        try {
            Path p = Paths.get(repoDir, ".git");
            Git git = null;
            if (!Files.exists(p))
                git = cloneRepository();
            else
                git = openRepository();

            assert git != null;

            if (tag != null && !tag.isEmpty()) {
                log.info("Checking out Tag");
                git.checkout().setName("refs/tags/" + tag).call();
            }

            closeRepository(git);
        } catch (GitAPIException | IOException e) {
            log.atError().withThrowable(e).log(e.getMessage());
        }
        log.info("Git execution complete");
    }

    @Override
    public String getToolName() {
        return GitConstants.GIT_CMD_NAME;
    }

    private Git cloneRepository() throws GitAPIException {
        log.info("Cloning Repo");
        log.info("Project: " + project.getProjectKey());
        log.info("Git URL: " + project.getSCM(SCMType.GIT).getURL());
        log.info("Project Path: " + project.getFullPath());
        File file = new File(project.getFullPath());
        if (!file.exists()) {
            file.mkdirs();
            file.mkdir();
        }

        return Git.cloneRepository()
                .setURI(project.getSCM(SCMType.GIT).getURL())
                .setDirectory(new File(project.getFullPath()))
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider(username, password))
                .call();
    }

    private Git openRepository() throws IOException {
        log.info("Opening Repo");
        return Git.open(new File(repoDir));
    }

    private void closeRepository(Git git) {
        log.info("Closing Repo");
        git.close();
    }
}
