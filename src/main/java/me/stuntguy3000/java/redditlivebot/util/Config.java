package me.stuntguy3000.java.redditlivebot.util;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.net.URL;
import java.util.HashMap;

// @author Luke Anderson | stuntguy3000
public class Config {

    private HashMap<String, String> configuration = new HashMap<>();

    public void loadConfig() throws IOException {
        initializeConfig();
        File configFile = new File("config.yml");

        if (configFile.exists()) {
            Yaml yaml = new Yaml();
            InputStream input = new FileInputStream(new File("config.yml"));
            configuration = (HashMap<String, String>) yaml.load(input);
            System.out.println("Loaded configuration.");
        } else {
            configFile.createNewFile();
            writeEmbeddedResourceToLocalFile("config.yml", configFile);
            System.out.println("Please modify config.yml!");
            System.exit(0);
        }
    }

    private void initializeConfig() {
        configuration.put("redditUsername", "");
        configuration.put("redditPassword", "");
        configuration.put("redditAppID", "");
        configuration.put("redditAppSecret", "");
        configuration.put("telegramKey", "");
    }

    public String getRedditUsername() {
        return configuration.get("redditUsername");
    }

    public String getRedditPassword() {
        return configuration.get("redditPassword");
    }

    public String getRedditAppID() {
        return configuration.get("redditAppID");
    }

    public String getRedditAppSecret() {
        return configuration.get("redditAppSecret");
    }

    public String getTelegramKey() {
        return configuration.get("telegramKey");
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

        // 1Kb buffer
        byte[] buffer = new byte[1024];
        int byteCount = 0;

        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = resourceUrl.openStream();
            outputStream = new FileOutputStream(configFile);

            while ((byteCount = inputStream.read(buffer)) >= 0) {
                outputStream.write(buffer, 0, byteCount);
            }

            // report success
            result = true;
        } catch (final IOException e) {
            System.out.println("Failure on saving the embedded resource " + resourceName + " to the file " + configFile.getAbsolutePath());
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (final IOException e) {
                    System.out.println("Problem closing an input stream while reading data from the embedded resource " + resourceName);
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.flush();
                    outputStream.close();
                } catch (final IOException e) {
                    System.out.println("Problem closing the output stream while writing the file " + configFile.getAbsolutePath());
                }
            }
        }

        return result;
    }
}
    