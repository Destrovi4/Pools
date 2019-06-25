package xyz.destr.pool;

import java.util.function.Consumer;

import xyz.destr.pool.functions.Releasable;

public final class LocalPool {

	public static <T extends Releasable<T>> T get(Class<T> clazz, Object... args) {
		return Pools.get().getPool(clazz, args).get();
	}
	
	public static <T> T get(PoolId<T> id) {
		return Pools.get().getPool(id).get();
	}
	
	public static <T> void accept(PoolId<T> id, T object) {
		Pools.get().getPool(id).accept(object);
	}
	
	public static <T extends Runnable & Consumer<A>, A> void execute(A param, Class<T> clazz, Object... args) {
		Pools.get().execute(param, clazz, args);
	}
	
	public static <T extends Runnable & Consumer<A>, A> void execute(A param, PoolId<T> id) {
		Pools.get().execute(param, id);
	}
	
	public static <T> Pool<T> getPool(PoolId<T> id){
		return Pools.get().getPool(id);
	}
	
	private LocalPool() {}
	
}
