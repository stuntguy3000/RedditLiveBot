package me.stuntguy3000.java.redditlivebot.handler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import lombok.Data;
import me.stuntguy3000.java.redditlivebot.object.pagination.PaginatedList;
import me.stuntguy3000.java.redditlivebot.object.pagination.PaginatedMessage;

/**
 * @author stuntguy3000
 */
@Data
public class PaginationHandler {
    private HashMap<UUID, PaginatedMessage> paginatedMessages = new HashMap<>();

    public PaginatedMessage createPaginatedMessage(List<String> content, int perPage) {
        PaginatedList paginatedList = new PaginatedList(content, perPage);

        PaginatedMessage paginatedMessage = new PaginatedMessage(paginatedList);
        paginatedMessages.put(paginatedMessage.getMessageID(), paginatedMessage);
        return paginatedMessage;
    }

    public PaginatedMessage getMessage(UUID uuid) {
        return paginatedMessages.get(uuid);
    }
}
