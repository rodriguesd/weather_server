package com.stockheap.weather.cache;

import com.stockheap.weather.WeatherConstants;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractCacheManager;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class FileSystemCacheManager extends AbstractCacheManager {

    private final String cacheDirectory = "cache"; // Default cache directory
    private final ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    @Override
    protected Collection<? extends Cache> loadCaches() {
        // Load initial cache(s)
        Cache defaultCache = new FileSystemCache(WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE, cacheDirectory);
        cacheMap.put(WeatherConstants.CacheNames.WEATHER_FORECAST_CACHE, defaultCache);
        return cacheMap.values();
    }

    @Override
    public Cache getCache(String name) {
        return cacheMap.computeIfAbsent(name, key -> {
            Cache newCache = new FileSystemCache(name, cacheDirectory);
            addCacheInternal(newCache); // Correctly adds cache
            return newCache;
        });
    }

    // Manually add a new cache if needed
    private void addCacheInternal(Cache cache) {
        cacheMap.put(cache.getName(), cache);
        updateCaches();
    }

    // Refreshes the cache list for AbstractCacheManager
    private void updateCaches() {
        super.getCacheNames().clear();
        super.getCacheNames().addAll(cacheMap.keySet());
    }
}