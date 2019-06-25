package xyz.destr.pool;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface Pool<T> extends Consumer<T>, Supplier<T> {
	
	public default T[] get(T[] store) {
		return get(store, 0, store.length);
	}
	
	public default T[] get(T[] store, int offset, int length) {
		for(int i = 0; i < length; i++) {
			store[offset + i] = get();
		}
		return store;
	}
	
	public default void accept(T[] store) {
		accept(store, 0, store.length);
	}
	
	public default void accept(T[] store, int offset, int length) {
		for(int i = 0; i < length; i++) {
			accept(store[offset + i]);
		}
	}
	
}