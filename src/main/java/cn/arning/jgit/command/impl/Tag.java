package cn.arning.jgit.command.impl;

import cn.arning.jgit.conf.GitUserConfig;
import cn.arning.jgit.command.Execute;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.transport.CredentialsProvider;
import org.eclipse.jgit.transport.PushResult;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class Tag implements Execute {

    @Autowired
    private GitUserConfig gitUserConfig;


    @Override
    public String execute(Git git, String message, String version) throws GitAPIException {
        CredentialsProvider credentialsProvider = new UsernamePasswordCredentialsProvider(gitUserConfig.getUsername(), gitUserConfig.getPassword());
        git.tag().setMessage(message).setName(version).call();
        Iterable<PushResult> origin = git.push().setPushTags().setRemote("origin").setCredentialsProvider(credentialsProvider).call();


        return null;
    }
}
