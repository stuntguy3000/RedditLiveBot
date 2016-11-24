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

package me.stuntguy3000.java.redditlivebot.object.pagination;

import lombok.Data;
import pro.zackpollard.telegrambot.api.chat.message.Message;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardButton;
import pro.zackpollard.telegrambot.api.keyboards.InlineKeyboardMarkup;

import java.util.LinkedList;
import java.util.UUID;

/**
 * @author stuntguy3000
 */
@Data
public class PaginatedMessage {
    private PaginatedList paginatedList;
    private Message message;
    private UUID messageID;

    public PaginatedMessage(PaginatedList paginatedList) {
        this.paginatedList = paginatedList;
        this.messageID = UUID.randomUUID();
    }

    public InlineKeyboardMarkup getButtons() {
        if (paginatedList.getPages() == 1) {
            return null;
        }

        LinkedList<InlineKeyboardButton> buttons = new LinkedList<>();

        if (paginatedList.getCurrentPage() > 1) {
            buttons.add(InlineKeyboardButton.builder()
                    .callbackData(messageID.toString() + "|prev")
                    .text("Previous").build());
        }

        buttons.add(InlineKeyboardButton.builder()
                .callbackData(messageID.toString() + "|ignore")
                .text("Page " + paginatedList.getCurrentPage() + "/" + paginatedList.getPages())
                .build());

        if (paginatedList.getCurrentPage() < paginatedList.getPages()) {
            buttons.add(InlineKeyboardButton.builder()
                    .callbackData(messageID.toString() + "|next")
                    .text("Next").build());
        }

        return InlineKeyboardMarkup.builder().addRow(buttons).build();
    }
}
