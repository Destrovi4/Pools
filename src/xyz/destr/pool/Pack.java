package xyz.destr.pool;

import java.util.function.Consumer;
import java.util.function.Supplier;

import xyz.destr.factory.Factory;

public class Pack<T> implements Consumer<T>, Supplier<T> {
		
	protected Object[] store;
	protected int filled = 0;
	
	public Pack(int size) {
		store = new Object[size];
	}
	
	public Pack(Integer size) {
		store = new Object[size];
	}
	
	public boolean isEmpty() {
		return filled == 0;
	}
	
	public boolean isFull() {
		return filled == store.length;
	}
	
	public int getCapacity() {
		return store.length - filled;
	}
	
	public int getSize() {
		return filled;
	}

	@SuppressWarnings("unchecked")
	@Override
	public T get() {
		// Элементы массива не zатераются, так как обьект в любом случае должен остаться в памяти.
		// В ходе использования будут переzписаны, либо могут хранить "мусорные" ссылки.
		return (T)store[--filled];
	}

	@Override
	public void accept(T obj) {
		store[filled++] = obj;
	}
	
	public void put(Pack<T> source, int count) {
		for(int i = 0; i < count; i++) {
			accept(source.get());
		}
	}

	public void fillHalf(Factory<T> factory) {
		final int count = store.length / 2;
		for(int i = filled; i < count; i++) {
			store[i] = factory.get();
		}
		filled = count;
	}

	public void balance(Pack<T> other) {
		if(store.length != other.store.length) {
			throw new RuntimeException();
		}
		Pack<T> big;
		Pack<T> small;
		if(this.filled > other.filled) {
			big = this;
			small = other;
		} else {
			big = other;
			small = this;
		}
		while(big.filled > small.filled) {
			small.accept(big.get());
		}
	}
	
}
