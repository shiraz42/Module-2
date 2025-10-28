package dogapi;

import java.util.*;

public class CachingBreedFetcher implements BreedFetcher {

    private final BreedFetcher fetcher;
    private final Map<String, List<String>> cache;
    private int callsMade = 0;

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
        this.cache = new HashMap<>();
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        try {
            List<String> result = fetcher.getSubBreeds(breed);
            callsMade++;
            cache.put(breed, result);
            return result;
        } catch (BreedNotFoundException e) {
            // don't cache failed results, just rethrow
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
