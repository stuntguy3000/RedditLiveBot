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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author stuntguy3000
 */
@Data
public class PaginatedList {
    private int currentPage = 1;
    private HashMap<Integer, List<String>> pages = new HashMap<>();

    /**
     * Create a new PaginatedList Object
     *
     * @param contentList List the list of all Strings to be paginated
     * @param perPage     Int the amount of items per page
     */
    public PaginatedList(List<String> contentList, int perPage) {
        int stringAmount = 0;
        int pageID = 0;
        List<String> currentPage = new ArrayList<>();

        for (String string : contentList) {
            stringAmount++;
            currentPage.add(string);

            if (stringAmount == perPage) {
                pageID++;
                pages.put(pageID, new ArrayList<>(currentPage));
                currentPage.clear();
                stringAmount = 0;
            }
        }

        if (stringAmount > 0) {
            if (pageID == 0) {
                pageID++;
            }

            pages.put(pageID, new ArrayList<>(currentPage));
        }
    }

    /**
     * Returns a page count
     *
     * @return Int the amount of pages
     */
    public int getPages() {
        return pages.size();
    }

    public String switchToNextPage() {
        currentPage++;
        if (pages.size() < currentPage) {
            currentPage = 1;
        }

        return process(pages.get(currentPage));
    }

    public String switchToPreviousPage() {
        currentPage--;
        if (currentPage < 1) {
            currentPage = pages.size();
        }

        return process(pages.get(currentPage));
    }

    public String getCurrentPageContent() {
        return process(pages.get(currentPage));
    }

    private String process(List<String> content) {
        if (content == null) {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (String line : content) {
            stringBuilder.append(line);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}
