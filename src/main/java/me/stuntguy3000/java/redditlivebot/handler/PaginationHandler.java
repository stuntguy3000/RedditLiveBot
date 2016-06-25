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
