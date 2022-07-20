package ru.otus.cache;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {
//Надо реализовать эти методы
    private final Map<K, V> weakHashMap = new WeakHashMap<>();

    private final List<HwListener<K, V>> listenerList = new ArrayList<>();

    public MyCache(){}

    @Override
    public void put(K key, V value) {
        notify(key, value, "put");
        weakHashMap.put(key, value);
    }

    @Override
    public void remove(K key) {
        notify(key,"remove");
        weakHashMap.remove(key);
    }

    @Override
    public V get(K key) {
        notify(key,"get");
        return weakHashMap.get(key);
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        listenerList.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        listenerList.remove(listener);
    }
    private void notify(K key, String action){
        notify(key, null, action);
    }
    private void notify(K key, V value, String action){
        try {
            listenerList.forEach(l -> l.notify(key, value, action));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
