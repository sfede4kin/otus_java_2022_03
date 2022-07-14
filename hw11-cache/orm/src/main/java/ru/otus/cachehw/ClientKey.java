package ru.otus.cachehw;

import java.util.Objects;

public class ClientKey {

    private final long key;

    public ClientKey(long key){
        this.key = key;
    }

    public long getKey(){
        return key;
    }

    @Override
    public String toString() {
        return "ClientKey{" +
                "key=" + key +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ClientKey clientKey = (ClientKey) o;
        return key == clientKey.key;
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

}
