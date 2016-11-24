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

package me.stuntguy3000.java.redditlivebot.handler;

import lombok.Data;
import me.stuntguy3000.java.redditlivebot.object.pagination.PaginatedList;
import me.stuntguy3000.java.redditlivebot.object.pagination.PaginatedMessage;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author stuntguy3000
 */
@Data
public class PaginationHandler {
    private HashMap<UUID, PaginatedMessage> paginatedMessages = new HashMap<>();

    /**
     * Creates a new PaginatedMessage object
     *
     * @param content List the content
     * @param perPage Integer the amount of pages to split content into
     *
     * @return PaginatedMessage the created object
     */
    public PaginatedMessage createPaginatedMessage(List<String> content, int perPage) {
        PaginatedList paginatedList = new PaginatedList(content, perPage);

        PaginatedMessage paginatedMessage = new PaginatedMessage(paginatedList);
        paginatedMessages.put(paginatedMessage.getMessageID(), paginatedMessage);
        return paginatedMessage;
    }

    /**
     * Returns a PaginatedMessage based upon the associated UUID value
     *
     * @param uuid UUID the uuid of the PaginatedMessage
     * @return PaginatedMessage the related message
     */
    public PaginatedMessage getMessage(UUID uuid) {
        return paginatedMessages.get(uuid);
    }
}
