package me.stuntguy3000.java.redditlivebot.handler;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;

import lombok.Getter;
import me.stuntguy3000.java.redditlivebot.object.config.BotSettings;
import me.stuntguy3000.java.redditlivebot.object.config.Subscriptions;

// @author Luke Anderson | stuntguy3000
public class ConfigHandler {

    @Getter
    private BotSettings botSettings = new BotSettings();
    @Getter
    private Subscriptions subscriptions = new Subscriptions();

    /**
     * Load configuration files and initiate config objects
     */
    public ConfigHandler() {
        loadFile("config.json");
        loadFile("subscriptions.json");

        if (subscriptions == null) {
            subscriptions = new Subscriptions();
        }
    }

    /**
     * Adds a feed ID to the known feed list
     *
     * @param id String the feed ID to add
     */
    public void addKnownFeed(String id) {
        if (!botSettings.getKnownLiveFeeds().contains(id.toLowerCase())) {
            botSettings.getKnownLiveFeeds().add(id.toLowerCase());
            saveConfig("config.json");
        }
    }

    /**
     * Loads a file into its related config object
     * <p>Filenames must be complete including file extension</p>
     *
     * @param fileName String the files name
     */
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

    /**
     * Save all configurations
     */
    public void saveConfigs() {
        saveConfig("config.json");
        saveConfig("subscriptions.json");
    }

    /**
     * Save a configuration
     * <p>Filenames must be complete including file extension</p>
     *
     * @param fileName String
     */
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

    /**
     * Save the subscriptions configuration
     */
    public void saveSubscriptions() {
        saveConfig("subscriptions.json");
    }

    /**
     * Set the current live feed
     *
     * @param id String the current live feed
     */
    public void setCurrentFeed(String id) {
        getBotSettings().setCurrentLiveFeed(id);
    }
}

    
