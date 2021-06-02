package Alpha.AlphaDataStructure;

import Alpha.Server.ClientMessage;

import java.util.Iterator;

public class CommentIterator<T> {
    private Iterator<T> iterator;
    private T currrentInstance;

    public CommentIterator(Iterator<T> iterator) {
        this.iterator = iterator;
    }

    public T first() {
        if (iterator.hasNext() && currrentInstance == null) {
            currrentInstance = iterator.next();
            return currrentInstance;
        }
        return null;
    }

    public boolean isDone() {
        return !iterator.hasNext() && currrentInstance == null;
    }

    public T next() {
        if (currrentInstance == null && iterator.hasNext()) {
            return first();
        }
        if (iterator.hasNext()) {
            currrentInstance = iterator.next();
        } else {
            currrentInstance = null;
        }
        return currrentInstance;

    }

    public T currrentInstance() {
        return currrentInstance;
    }
}
