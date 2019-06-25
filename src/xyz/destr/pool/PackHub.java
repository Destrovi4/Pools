package xyz.destr.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.function.Consumer;
import java.util.function.Supplier;

import xyz.destr.factory.Factory;

public class PackHub<T> implements Consumer<Pack<T>>, Supplier<Pack<T>> {
	
	private static HashMap<PoolId<?>, PackHub<?>> hubByPoolId = new HashMap<>();
	
	public static synchronized <T> PackHub<T> get(PoolId<T> poolId) {
		@SuppressWarnings({ "unchecked" })
		final PackHub<T> hub = (PackHub<T>) hubByPoolId.get(poolId);
		if(hub == null) {
			final PackHub<T> newHub = new PackHub<T>(poolId.getFactory());
			hubByPoolId.put(poolId, newHub);
			return newHub;
		} else {
			return hub;
		}
	}
	
	protected final Factory<T> factory;
	protected ArrayList<Pack<T>> packList = new ArrayList<>();
	protected ArrayList<Pack<T>> emptyPackList = new ArrayList<>();
	
	public PackHub(Factory<T> factory) {
		this.factory = factory;
	}
	
	public Factory<T> getFactory(){
		return factory;
	}
	
	protected synchronized Pack<T> internalGet() {
		if(packList.isEmpty()) {
			return null;
		} else {
			return packList.remove(packList.size() - 1);
		}
	}
	
	protected synchronized Pack<T> getEmptyPack() {
		if(emptyPackList.isEmpty()) {
			return new Pack<T>(32);
		} else {
			return emptyPackList.remove(emptyPackList.size() - 1);
		}
	}
	
	protected synchronized void putEmptyPack(Pack<T> pack) {
		emptyPackList.add(pack);
	}
	
	@Override
	public Pack<T> get() {
		final Pack<T> pack = internalGet();
		if(pack == null) {
			final Pack<T> newPack = getEmptyPack();
			newPack.fillHalf(factory);
			return newPack;
		}
		return pack;
	}
	
	public synchronized void innerAccept(Pack<T> pack) {
		packList.add(pack);
	}

	@Override
	public void accept(Pack<T> pack) {
		if(pack.isEmpty()) {
			putEmptyPack(pack);
		} else {
			Pack<T> newPack = getEmptyPack();
			pack.balance(newPack);
			innerAccept(pack);
			innerAccept(newPack);
		}
	}

	public Pack<T> swap(Pack<T> pack) {
		accept(pack);
		return get();
	}
	
}
