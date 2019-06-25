package xyz.destr.pool;

public class BasicPool<T> implements Pool<T> {

	protected PackHub<T> hub;
	protected Pack<T> pack;
	
	protected BasicPool(PoolId<T> id) {
		this.hub = PackHub.get(id);
		this.pack = hub.get();
	}
		
	@Override
	public void accept(T obj) {
		pack.accept(obj);
		if(pack.isFull()) {
			pack = hub.swap(pack);
		}
	}

	@Override
	public T get() {
		try {
			return pack.get();
		} finally {
			if(pack.isEmpty()) {
				pack = hub.swap(pack);
			}
		}
	}
}
