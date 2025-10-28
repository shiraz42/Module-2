package dogapi;

import java.util.*;

/**
 * This BreedFetcher caches fetch request results to improve performance and
 * lessen the load on the underlying data source. An implementation of BreedFetcher
 * must be provided. The number of calls to the underlying fetcher are recorded.
 *
 * If a call to getSubBreeds produces a BreedNotFoundException, then it is NOT cached
 * in this implementation. The provided tests check for this behaviour.
 *
 * The cache maps the name of a breed to its list of sub breed names.
 */
public class CachingBreedFetcher implements BreedFetcher {

    private final BreedFetcher fetcher;                 // underlying fetcher
    private final Map<String, List<String>> cache;      // caching map
    private int callsMade = 0;                          // counter

    public CachingBreedFetcher(BreedFetcher fetcher) {
        this.fetcher = fetcher;
        this.cache = new HashMap<>();
    }

    @Override
    public List<String> getSubBreeds(String breed) throws BreedNotFoundException {
        // Check the cache first
        if (cache.containsKey(breed)) {
            return cache.get(breed);
        }

        // Otherwise, fetch from the underlying fetcher
        try {
            List<String> result = fetcher.getSubBreeds(breed);
            callsMade++;                                // count the API call
            cache.put(breed, result);                   // cache the result
            return result;
        } catch (BreedNotFoundException e) {
            // DO NOT cache this
            throw e;
        }
    }

    public int getCallsMade() {
        return callsMade;
    }
}
