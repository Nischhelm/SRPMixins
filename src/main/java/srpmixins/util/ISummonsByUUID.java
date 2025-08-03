package srpmixins.util;

import java.util.Map;
import java.util.UUID;

public interface ISummonsByUUID {
    Map<UUID, Integer> srpmixins$getSummonEntries();
    void srpmixins$addSummon(UUID id, int points);
}
