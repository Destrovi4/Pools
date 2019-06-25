package xyz.destr.pool;

import java.util.HashMap;

import xyz.destr.factory.Factory;
import xyz.destr.factory.FactorySignature;
import xyz.destr.factory.Factorys;

public final class PoolId<T> {
	
	private static HashMap<FactorySignature<?>, PoolId<?>> idByFactorySignature = new HashMap<>();
	
	public static <T> PoolId<T> get(Class<T> clazz, Object... args) {
		return get(FactorySignature.get(clazz, args));
	}
	
	public static synchronized <T> PoolId<T> get(FactorySignature<T> signature) {
		@SuppressWarnings("unchecked")
		PoolId<T> id = (PoolId<T>)idByFactorySignature.get(signature);
		if(id == null) {
			int index = idByFactorySignature.size();
			PoolId<T> newId = new PoolId<T>(index + 1, signature);
			idByFactorySignature.put(signature, newId);
			return newId;
		} else {
			return id;
		}
	}
	
	protected final int id;
	private final FactorySignature<T> signature;
	private final Factory<T> factory;
	
	private PoolId(int id, FactorySignature<T> signature) {
		this.id = id;
		this.signature = signature;
		this.factory = Factorys.get(signature);
	}
	
	public int getId() {
		return id;
	}
	
	public int getIndex() {
		return id - 1;
	}
	
	public FactorySignature<T> getSignature() {
		return signature;
	}

	public Factory<T> getFactory() {
		return factory;
	}
	
}
