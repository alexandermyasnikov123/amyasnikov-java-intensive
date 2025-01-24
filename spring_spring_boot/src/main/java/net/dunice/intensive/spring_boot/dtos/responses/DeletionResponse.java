package net.dunice.intensive.spring_boot.dtos.responses;

import java.util.List;

public record DeletionResponse<K>(
        Integer numberDeleted,
        List<K> deletedIds
) {
    public DeletionResponse(List<K> deletedIds) {
        this(deletedIds.size(), deletedIds);
    }
}
