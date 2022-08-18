/*
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
package dev.siliconcode.arc.experimenter.impl.git

import org.eclipse.jgit.api.Git

/**
 * @author Isaac Griffith
 * @version 1.3.0
 */
class GitWorker implements RepoWorker {

    Git git

    private GitWorker() {

    }

    static GitWorker instance() {
        return GitWorkerHolder.INSTANCE
    }

    private static class GitWorkerHolder {
        private static final GitWorker INSTANCE = new GitWorker()
    }

    @Override
    void clone(String uri, String path) throws RepoWorkerException {
        try {
            git = Git.cloneRepository()
                    .setURI(uri)
                    .setDirectory(new File(path))
                    .call()
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }

    @Override
    void branch(String branch) throws RepoWorkerException {
        try {
            git.branchCreate().setName(branch).call()
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }

    @Override
    void tag(String tag) throws RepoWorkerException {
        try {
            git.tag().setName(tag).call()
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }

    @Override
    void addAll() throws RepoWorkerException {
        try {
            git.add()
                    .addFilepattern(".")
                    .call()
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }

    @Override
    void commit(String message, String email, String name) throws RepoWorkerException {
        try {
            git.commit().setMessage(message).setAuthor(email, name).call()
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }

    @Override
    void checkout(String name, boolean tag) throws RepoWorkerException {
        try {
            if (tag) {
                git.checkout()
                        .setName("refs/heads/" + name)
                        .call()
            } else {
                git.checkout()
                        .setName("refs/tags/" + name)
                        .call()
            }
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }

    @Override
    void push() throws RepoWorkerException {
        try {
            git.push().call()
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }

    @Override
    void pull() throws RepoWorkerException {
        try {
            git.pull().call()
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }

    @Override
    void fetch() throws RepoWorkerException {
        try {
            git.fetch().call()
        } catch (Exception e) {
            throw new RepoWorkerException(e.getMessage())
        }
    }
}
