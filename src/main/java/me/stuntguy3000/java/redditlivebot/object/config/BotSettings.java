/*
 * MIT License
 *
 * Copyright (c) 2016 Luke Anderson
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

package me.stuntguy3000.java.redditlivebot.object.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

// @author Luke Anderson | stuntguy3000
@Data
public class BotSettings {
    private Boolean autoUpdater;
    private long lastPost;
    private String currentLiveFeed;
    private Boolean debugMode;
    private List<String> knownLiveFeeds;
    private List<Long> telegramAdmins;
    private String telegramKey;

    public BotSettings() {
        this.telegramKey = "";
        this.telegramAdmins = new ArrayList<>();
        this.knownLiveFeeds = new ArrayList<>();
        this.autoUpdater = true;
        this.debugMode = false;
    }
}
    