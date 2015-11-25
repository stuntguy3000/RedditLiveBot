package me.stuntguy3000.java.redditlivebot.util;

import com.google.gson.Gson;
import lombok.Getter;

import java.io.*;
import java.net.URL;

// @author Luke Anderson | stuntguy3000
public class Config {

    @Getter
    public BotSettings botSettings;

    public void loadConfig() throws IOException {
        Gson gson = new Gson();
        File configFile = new File("config.json");

        if (configFile.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            botSettings = gson.fromJson(br, BotSettings.class);

            LogHandler.log("Loaded configuration.");
        } else {
            configFile.createNewFile();
            writeEmbeddedResourceToLocalFile("config.json", configFile);
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

    