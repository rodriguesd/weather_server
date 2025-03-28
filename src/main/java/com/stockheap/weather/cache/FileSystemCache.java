package com.stockheap.weather.cache;

import org.springframework.cache.Cache;

import java.io.*;
import java.util.concurrent.Callable;

public class FileSystemCache implements Cache {

    private final String name;
    private final String cacheDirectory;

    public FileSystemCache(String name, String cacheDirectory) {
        this.name = name;
        this.cacheDirectory = cacheDirectory;
        File dir = new File(cacheDirectory);
        if(!dir.exists()){
            dir.mkdirs();
        }
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return null;
    }

    @Override
    public ValueWrapper get(Object key) {
        File file = new File(cacheDirectory, generateFileName(key));
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                return (ValueWrapper) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                // Handle exception, potentially log it
                return null;
            }
        }
        return null;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        ValueWrapper wrapper = get(key);
        return (wrapper != null && type.isInstance(wrapper.get())) ? type.cast(wrapper.get()) : null;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        ValueWrapper wrapper = get(key);
        if (wrapper != null) {
            return (T) wrapper.get();
        }
        synchronized (this) {
            ValueWrapper existingWrapper = get(key);
            if (existingWrapper != null) {
                return (T) existingWrapper.get();
            }
            try {
                T value = valueLoader.call();
                put(key, value);
                return value;
            } catch (Exception e) {
                throw new RuntimeException("Failed to load value for key: " + key, e);
            }
        }
    }

    @Override
    public void put(Object key, Object value) {
        File file = new File(cacheDirectory, generateFileName(key));
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(new SimpleValueWrapper(value));
        } catch (IOException e) {
            // Handle exception, potentially log it
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        if (get(key) == null) {
            put(key, value);
            return null;
        } else {
            return get(key);
        }
    }

    @Override
    public void evict(Object key) {
        File file = new File(cacheDirectory, generateFileName(key));
        if (file.exists()) {
            file.delete();
        }
    }

    @Override
    public void clear() {
        File dir = new File(cacheDirectory);
        if(dir.exists() && dir.isDirectory()){
            File[] files = dir.listFiles();
            if(files != null){
                for(File file : files){
                    file.delete();
                }
            }
        }
    }

    private String generateFileName(Object key) {
        return name + "_" + key.toString().hashCode() + ".cache";
    }

    static class SimpleValueWrapper implements Cache.ValueWrapper, Serializable {
        private final Object value;

        public SimpleValueWrapper(Object value) {
            this.value = value;
        }

        @Override
        public Object get() {
            return value;
        }
    }
}