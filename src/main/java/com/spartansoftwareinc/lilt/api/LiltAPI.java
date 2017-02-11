package com.spartansoftwareinc.lilt.api;

import java.io.IOException;
import java.util.List;

public interface LiltAPI {

    Memory getMemory(long memoryId) throws IOException;

    List<Memory> getAllMemories() throws IOException;

    /**
     * Returns simple translation data.
     * @param memoryId
     * @param source
     * @param count
     * @return a list of translation strings. The length of the list should be determined by the value of
     *         the count parameter.
     * @throws IOException
     */
    List<String> getSimpleTranslation(long memoryId, String source, int count) throws IOException;

    void updateTranslation(long memoryId, String source, String target) throws IOException;
}
