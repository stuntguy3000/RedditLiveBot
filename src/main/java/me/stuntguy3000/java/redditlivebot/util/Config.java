package me.stuntguy3000.java.redditlivebot.util;

import com.google.gson.Gson;
import lombok.Getter;

import java.io.*;
import java.net.URL;

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
            LogHandler.log("RedditUsername: " + botSettings.getRedditUsername());
            LogHandler.log("RedditPassword: " + botSettings.getRedditPassword());
            LogHandler.log("RedditAppID: " + botSettings.getRedditAppID());
            LogHandler.log("RedditAppSecret: " + botSettings.getRedditAppSecret());
            LogHandler.log("telegramKey: " + botSettings.getTelegramKey());
            LogHandler.log("getTelegramAdmins: " + botSettings.getTelegramAdmins().size());
            LogHandler.log("Loaded configuration.");
        } else {
            GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
            gson = builder.create();
            String json = gson.toJson(config);
    
            FileOutputStream outputStream;
    
            try {
                
                configFile.createNewFile();
                outputStream = new FileOutputStream(configFile);
                outputStream.write(json.getBytes());
                outputStream.close();
    
                return true;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                instance.getLogger().severe("The config could not be saved as the file couldn't be found on the storage device. Please check the directories read/write permissions and contact the developer!");
            } catch (IOException e) {
                e.printStackTrace();
                instance.getLogger().severe("The config could not be written to as an error occured. Please check the directories read/write permissions and contact the developer!");
            }
            LogHandler.log("Please modify config.json!");
            System.exit(0);
        }
    }

    /**
     * Writes an embedded resource to a local file by the resource name.
     *
     * @param resourceName an embedded resource name
     * @param configFile   a file we write the resource to
     * @return true when write was successful and false otherwise
     * @author http://www.shirmanov.com/2011/05/write-java-embedded-resource-to-local.html
     */
    public boolean writeEmbeddedResourceToLocalFile(final String resourceName, final File configFile) {
        boolean result = false;

        final URL resourceUrl = getClass().getClassLoader().getResource(resourceName);

        byte[] buffer = new byte[1024];
        int byteCount;

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = resourceUrl.openStream();
            outputStream = new FileOutputStream(configFile);

            while ((byteCount = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, byteCount);
            }

            result = true;
        } catch (final IOException e) {
            LogHandler.log("Failure on saving the embedded resource " + resourceName + " to the file " + configFile.getAbsolutePath());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    LogHandler.log("Problem closing an input stream while reading data from the embedded resource " + resourceName);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (final IOException e) {
                    LogHandler.log("Problem closing the output stream while writing the file " + configFile.getAbsolutePath());
                }
            }
        }

        return result;
    }
}

    
