package me.stuntguy3000.java.redditlivebot.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;

import java.io.*;

// @author Luke Anderson | stuntguy3000
public class Config {

    @Getter
    private final BotSettings botSettings;

    public Config() {
        Gson gson = new Gson();
        File configFile = new File("config.json");

        if (configFile.exists()) {
            BufferedReader br = null;
            try {
                br = new BufferedReader(new FileReader(configFile));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            botSettings = gson.fromJson(br, BotSettings.class);
        } else {
            botSettings = new BotSettings();

            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            gson = builder.create();
            String json = gson.toJson(botSettings);

            FileOutputStream outputStream;

            try {
                configFile.createNewFile();
                outputStream = new FileOutputStream(configFile);
                outputStream.write(json.getBytes());
                outputStream.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                LogHandler.log("The config could not be saved as the file couldn't be found on the storage device. Please check the directories read/write permissions and contact the developer!");
            } catch (IOException e) {
                e.printStackTrace();
                LogHandler.log("The config could not be written to as an error occurred. Please check the directories read/write permissions and contact the developer!");
            }
            LogHandler.log("Please modify config.json!");
            System.exit(0);
        }
    }
}

    
