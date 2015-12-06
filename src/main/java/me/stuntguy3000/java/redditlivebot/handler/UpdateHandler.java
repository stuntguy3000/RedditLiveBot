package me.stuntguy3000.java.redditlivebot.handler;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.util.LogHandler;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by bo0tzz
 */
public class UpdateHandler implements Runnable {

    private RedditLiveBot instance;
    private String projectName;

    public UpdateHandler(RedditLiveBot instance, String projectName) {
        this.instance = instance;
        this.projectName = projectName;
    }

    @Override
    public void run() {
        File build = new File("build");
        File jar = new File(projectName + ".new");
        int currentBuild = RedditLiveBot.BUILD;
        int newBuild = 0;

        while (true) {
            try {
                HttpResponse<String> response = Unirest.get("http://ci.zackpollard.pro/job/" + projectName + "/lastSuccessfulBuild/buildNumber").asString();

                if (response.getStatus() == 200) {
                    newBuild = Integer.parseInt(response.getBody());
                } else {
                    LogHandler.log("[ERROR] Updater status code: " + response.getStatus());
                    instance.sendToAdmins("[ERROR] Updater status code: " + response.getStatus() + "\n\nUpdater stopped.");
                    instance.stopUpdater();
                }
            } catch (UnirestException e) {
                e.printStackTrace();
                instance.stopUpdater();
            }

            if (newBuild > currentBuild) {
                LogHandler.log("Downloading build #" + newBuild);
                instance.sendToAdmins("Downloading build #" + newBuild);
                try {
                    FileUtils.writeStringToFile(build, String.valueOf(newBuild));
                    FileUtils.copyURLToFile(new URL("http://ci.zackpollard.pro/job/" + projectName + "/lastSuccessfulBuild/artifact/target/" + projectName + ".jar"), jar);
                    LogHandler.log("Build #" + newBuild + " downloaded. Restarting...");
                    instance.sendToAdmins("Build #" + newBuild + " downloaded. Restarting...");
                } catch (IOException e) {
                    System.err.println("Updater failed!");
                    e.printStackTrace();
                    break;
                }
                System.exit(0);
            }
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}