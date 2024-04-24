package org.liny.linycoreserverevents.Utils;

public class Pair<K, V> implements Cloneable {
    private V value; private K key;

    public Pair (K key, V value) {
        this.key = key; this.value = value;
    }

    public K getKey() {
        return this.key;
    }

    public V getValue() {
        return this.value;
    }

    protected void setValue (V value) {
        this.value = value;
    }
    protected void setKey (K key) {
        this.key = key;
    }

    @Override
    public Pair<K, V> clone() {
        try {
            Pair<K, V> clonedPair = (Pair<K, V>) super.clone();
            clonedPair.setKey(this.key); clonedPair.setValue(this.value);
            return clonedPair;
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e);
        }
    }
}
