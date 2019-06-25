package xyz.destr.pool;

import xyz.destr.factory.FactorySignature;

public class ProxyPool<T> implements Pool<T> {

	protected final PoolId<T> id;
	
	public ProxyPool(Class<T> clazz, Object... args) {
		this(FactorySignature.get(clazz, args));
	}
	
	public ProxyPool(FactorySignature<T> signature) {
		this(PoolId.get(signature));
	}
	
	public ProxyPool(PoolId<T> id) {
		this.id = id;
	}
	
	@Override
	public void accept(T object) {
		LocalPool.accept(id, object);
	}

	@Override
	public T get() {
		return LocalPool.get(id);
	}

	public Pool<T> getRealPool() {
		return LocalPool.getPool(id);
	}

}
