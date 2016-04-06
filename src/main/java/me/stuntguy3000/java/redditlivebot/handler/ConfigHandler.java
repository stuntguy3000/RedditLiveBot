package me.stuntguy3000.java.redditlivebot.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.object.config.BotSettings;
import me.stuntguy3000.java.redditlivebot.object.config.Subscriptions;

import java.io.*;

// @author Luke Anderson | stuntguy3000
public class ConfigHandler {

    @Getter
    private BotSettings botSettings = new BotSettings();
    @Getter
    private Subscriptions subscriptions = new Subscriptions();

    public ConfigHandler() {
        loadFile("config.json");
        loadFile("subscriptions.json");

        if (subscriptions == null) {
            subscriptions = new Subscriptions();
        }
    }

    public void addFeed(String id) {
        if (!botSettings.getKnownLiveFeeds().contains(id.toLowerCase())) {
            botSettings.getKnownLiveFeeds().add(id.toLowerCase());
            saveConfig("config.json");
        }
    }

    private void loadFile(String fileName) {
        Gson gson = new Gson();
        File configFile = new File(fileName);

        if (configFile.exists()) {
            BufferedReader br;
            try {
                br = new BufferedReader(new FileReader(configFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }

            switch (fileName.split(".json")[0].toLowerCase()) {
                case "config": {
                    botSettings = gson.fromJson(br, BotSettings.class);
                }
                case "subscriptions": {
                    subscriptions = gson.fromJson(br, Subscriptions.class);
                }
            }
        } else {
            saveConfig(fileName);
        }
    }

    public void saveConfig() {
        saveConfig("config.json");
    }

    private void saveConfig(String fileName) {
        File configFile = new File(fileName);
        GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
        Gson gson = builder.create();
        String json = null;

        switch (fileName.split(".json")[0].toLowerCase()) {
            case "config": {
                json = gson.toJson(botSettings);
                break;
            }
            case "subscriptions": {
                json = gson.toJson(subscriptions);
                break;
            }
        }

        FileOutputStream outputStream;

        try {
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            outputStream = new FileOutputStream(configFile);
            assert json != null;
            outputStream.write(json.getBytes());
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            LogHandler.log("The config could not be saved as the file couldn't be found on the storage device. Please check the directories read/write permissions and contact the developer!");
        } catch (IOException e) {
            e.printStackTrace();
            LogHandler.log("The config could not be written to as an error occurred. Please check the directories read/write permissions and contact the developer!");
        } catch (NullPointerException e) {
            e.printStackTrace();
            LogHandler.log("Invalid Config Specified! Please check the directories read/write permissions and contact the developer!");
        }
    }

    public void saveSubscriptions() {
        saveConfig("subscriptions.json");
    }

    public void setCurrentFeed(String id) {
        getBotSettings().setCurrentLiveFeed(id);
    }
}

    
