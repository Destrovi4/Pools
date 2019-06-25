package xyz.destr.pool;

import java.util.Arrays;
import java.util.function.Consumer;

public final class Pools {
	
	private static ThreadLocal<Pools> threadLocalPools = ThreadLocal.withInitial(()->new Pools());
		
	public static Pools get() {
		return threadLocalPools.get();
	}
	
	private Pool<?>[] pools = new Pool<?>[0];
	
	private Pools() {}
	
	public <T> Pool<T> getPool(Class<T> clazz, Object... args) {
		return getPool(PoolId.get(clazz, args));
	}
		
	@SuppressWarnings("unchecked")
	public <T> Pool<T> getPool(PoolId<T> id) {
		final int index = id.getIndex();
		if(pools.length <= index) {
			pools = Arrays.copyOf(pools, index + 1);
			Pool<T> newPool = new BasicPool<>(id); 
			pools[index] = newPool;
			return newPool; 
		} else {
			Pool<T> pool = (Pool<T>)pools[index];
			if(pool == null) {
				Pool<T> newPool = new BasicPool<>(id); 
				pools[index] = newPool;
				return newPool;
			}
			return pool;
		}
	}
	
	public <T extends Runnable & Consumer<A>, A> void execute(A param, Class<T> clazz, Object... args) {
		execute(param, PoolId.get(clazz, args));
	}
	
	public <T extends Runnable & Consumer<A>, A> void execute(A param, PoolId<T> id) {
		@SuppressWarnings("unchecked")
		Executor<T, A> executor = getPool(Executor.EXECUTOR_POOL_ID).get();
		executor.accept(param);
		executor.execute(id);
	}
}