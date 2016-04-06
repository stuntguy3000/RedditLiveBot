package me.stuntguy3000.java.redditlivebot.handler;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.stuntguy3000.java.redditlivebot.RedditLiveBot;
import me.stuntguy3000.java.redditlivebot.object.Lang;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Created by bo0tzz
 */
public class UpdateHandler implements Runnable {

    private final String fileName;
    private final String projectName;

    public UpdateHandler(String projectName, String fileName) {
        this.projectName = projectName;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        File build = new File("build");
        File jar = new File(fileName + ".new");
        int currentBuild = RedditLiveBot.BUILD;
        int newBuild;

        while (true) {
            try {
                HttpResponse<String> response = Unirest.get("http://ci.zackpollard.pro/job/" + projectName + "/lastSuccessfulBuild/buildNumber").asString();

                if (response.getStatus() == 200) {
                    newBuild = Integer.parseInt(response.getBody());
                } else {
                    Lang.sendAdmin("[UPDATER] [ERROR] Updater status code: " + response.getStatus());
                    return;
                }
            } catch (UnirestException e) {
                //e.printStackTrace();
                return;
            }

            if (newBuild > currentBuild) {
                Lang.sendAdmin("[UPDATER] Downloading build #" + newBuild);
                try {
                    FileUtils.writeStringToFile(build, String.valueOf(newBuild));
                    FileUtils.copyURLToFile(new URL("http://ci.zackpollard.pro/job/" + projectName + "/lastSuccessfulBuild/artifact/target/" + fileName + ".jar"), jar);
                    Lang.sendAdmin("[UPDATER] Build #" + newBuild + " downloaded. Restarting...");
                } catch (IOException e) {
                    Lang.sendAdmin("[UPDATER] Updater failed!");
                    e.printStackTrace();
                    return;
                }

                RedditLiveBot.getInstance().shutdown();
            }
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}