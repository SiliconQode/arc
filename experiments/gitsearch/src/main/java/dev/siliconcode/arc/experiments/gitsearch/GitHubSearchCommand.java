/**
 * The MIT License (MIT)
 *
 * Empirilytics GitHub Search Tool
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
/**
 * This copy of Woodstox XML processor is licensed under the
 * Apache (Software) License, version 2.0 ("the License").
 * See the License for details about distribution rights, and the
 * specific rights regarding derivate works.
 *
 * You may obtain a copy of the License at:
 *
 * http://www.apache.org/licenses/
 *
 * A copy is also included in the downloadable source code package
 * containing Woodstox, in file "ASL2.0", under the same directory
 * as this file.
 */
package dev.siliconcode.arc.experiments.gitsearch;

import com.google.common.collect.Lists;
import dev.siliconcode.arc.datamodel.Project;
import dev.siliconcode.arc.datamodel.SCM;
import dev.siliconcode.arc.datamodel.SCMType;
import dev.siliconcode.arc.datamodel.System;
import lombok.extern.log4j.Log4j2;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHTag;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.PagedSearchIterable;

import java.io.IOException;
import java.util.List;

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
@Log4j2
public class GitHubSearchCommand {

    private GitHub github;
    private String user;
    private String token;

    public GitHubSearchCommand(String user, String token) {
//        super("GitHub Search");
        this.user = user;
        this.token = token;
    }

    public void getAuth() {
        try {
            github = GitHub.connect(user, token);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<System> findProjects(int maxProj, int minSize, int maxSize, int minStars, int minTags) {
        final int[] numProj = {0};

        List<System> systems = Lists.newCopyOnWriteArrayList();

        if (github != null) {
            PagedSearchIterable<GHRepository> repos = github.searchRepositories()
                    .size(">" + minSize)// + ", <" + maxSize)
                    .language("java")
                    .stars(">" + minStars)
                    .list()
                    .withPageSize(100);

            for(GHRepository repo : repos) {
                if (repo.getSize() > minSize && repo.getSize() < maxSize) {
                    try {
                        List<GHTag> tags = repo.listTags().withPageSize(10).asList();
                        if (tags.size() >= minTags) {
                            System sys = System.builder()
                                    .name(repo.getName())
                                    .key(repo.getName())
                                    .create();
                            sys.saveIt();
                            systems.add(sys);

                            tags.forEach(tag -> {
                                Project p = Project.builder()
                                        .name(repo.getName())
                                        .version(tag.getName())
                                        .projKey(repo.getName() + ":" + tag.getName())
                                        .create();
                                p.saveIt();
                                sys.addProject(p);
                                numProj[0] += 1;

                                SCM scm = SCM.builder()
                                        .name(repo.getName())
                                        .url(repo.getHtmlUrl().toString())
                                        .type(SCMType.GIT)
                                        .tag(tag.getName())
                                        .create();
                                scm.saveIt();
                                p.addSCM(scm);
                            });
                        }
                        if (numProj[0] > maxProj)
                            break;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            log.info("Repos Found: " + numProj[0]);
        }

        return systems;
    }

    private String toJson(System sys) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        builder.append(String.format("\"name\": \"%s\",%n", sys.getName()));
        builder.append(String.format("\"key\": \"%s\", %n", sys.getKey()));
        builder.append("\"projects\": [\n");
        List<Project> projects = sys.getProjects();
        for (Project proj : projects) {
            builder.append("{");
            builder.append(toJson(proj));
            builder.append("}");
        }
        builder.append("]\n");
        builder.append("}");

        return builder.toString();
    }

    private String toJson(Project proj) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\"name\": \"%s\",%n", proj.getName()));
        builder.append(String.format("\"key\": \"%s\",%n", proj.getProjectKey()));
        builder.append(String.format("\"version\": \"%s\",%n", proj.getVersion()));
        proj.getLanguages();
        List<SCM> scms = proj.getSCMs();
        builder.append("\"scms\": [\n");
        for (SCM scm : scms) {
            builder.append("{\n");
            builder.append(toJson(scm));
            builder.append("}");
        }
        builder.append("]\n");

        return builder.toString();
    }

    private String toJson(SCM scm) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format("\"branch\": \"%s\",%n", scm.getBranch()));
        builder.append(String.format("\"key\": \"%s\",%n", scm.getSCMKey()));
        builder.append(String.format("\"url\": \"%s\",%n", scm.getURL()));
        builder.append(String.format("\"tag\": \"%s\",%n", scm.getTag()));
        builder.append(String.format("\"type\": \"%s\",%n", scm.getType()));
        return builder.toString();
    }

    public static class GitCloneCommand {
    }
}
