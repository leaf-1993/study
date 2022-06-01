package com.cx.ds;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * @author chenxiang
 * @create 2022-01-16 15:34
 */
public interface IMap<K, V> {
    // Query Operations

    int size();

    boolean isEmpty();

    boolean containsKey(Object key);

    boolean containsValue(Object value);

    V get(Object key);

    // Modification Operations

    V put(K key, V value);

    /**
     * @param key
     * @return V 被移除的值
     */
    V remove(Object key);

    // Bulk Operations 批量操作

    void putAll(IMap<? extends K, ? extends V> m);

    void clear();

    // Views 查看

    Set<K> keySet();

    Collection<V> values();

    Set<IMap.IEntry<K, V>> entrySet();

    interface IEntry<K, V> {
        K getKey();

        V getValue();

        /**
         * @param value
         * @return 旧的value
         */
        V setValue(V value);

        @Override
        boolean equals(Object o);

        @Override
        int hashCode();

        public static <K extends Comparable<? super K>, V> Comparator<IMap.IEntry<K, V>> comparingByKey() {
            return Comparator.comparing(IEntry::getKey);
        }

        public static <K, V extends Comparable<? super V>> Comparator<IMap.IEntry<K, V>> comparingByValue(){
            return Comparator.comparing(IEntry::getValue);
        }

        public static <K, V> Comparator<IMap.IEntry<K, V>> comparingByKey(Comparator<? super K> cmp){
            Objects.requireNonNull(cmp);
            return (c1, c2) -> cmp.compare(c1.getKey(), c2.getKey());
        }

        public static <K, V> Comparator<IMap.IEntry<K, V>> comparingByValue(Comparator<? super V> cmp){
            Objects.requireNonNull(cmp);
            return (c1, c2) -> cmp.compare(c1.getValue(), c2.getValue());
        }


    }

    // Comparison and hashing

    @Override
    boolean equals(Object o);

    @Override
    int hashCode();

    // Defaultable methods

    default V getOrDefault(Object key, V defaultValue) {
        V v;
        return ((v = get(key)) != null || containsKey(key))
                ? v
                : defaultValue;
    }

    default void foreach(BiConsumer<? super K, ? super V> action){
        Objects.requireNonNull(action);
        for(IMap.IEntry<K, V> entry : entrySet()){
            K k;
            V v;
            try {
                k = entry.getKey();
                v = entry.getValue();
            } catch (IllegalStateException ise){
                throw new ConcurrentModificationException(ise);
            }
            action.accept(k, v);
        }
    }

    default void replaceAll(BiFunction<? super K, ? super V, ? extends V> function){

    }
}
