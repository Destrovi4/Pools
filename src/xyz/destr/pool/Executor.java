package xyz.destr.pool;

import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class Executor<T extends Runnable & Consumer<A>, A> implements Runnable, Consumer<A> {
	
	private static ExecutorService defaultExecutorService = null;
	@SuppressWarnings("rawtypes")
	public static final PoolId<Executor> EXECUTOR_POOL_ID = PoolId.get(Executor.class);
	
	public static synchronized ExecutorService getDefaultExecutorService() {
		if(defaultExecutorService == null) {
			defaultExecutorService = Executors.newCachedThreadPool();
		}
		return defaultExecutorService;
	}
	
	public static synchronized void setDefaultExecutorService(ExecutorService es) {
		if(defaultExecutorService == null) {
			defaultExecutorService = es;
		} else {
			throw new AssertionError("defaultExecutorService should be initialized once");
		}
	}
	
	private static HashMap<PoolId<? extends Runnable>, ExecutorService> executorServiceByPoolId = new HashMap<>();
	
	public synchronized static ExecutorService getExecutorService(PoolId<? extends Runnable> poolId) {
		final ExecutorService es = executorServiceByPoolId.get(poolId);
		if(es == null) {
			final ExecutorService newES = getDefaultExecutorService();
			executorServiceByPoolId.put(poolId, newES);
			return newES;
		} else {
			return es;
		}
	}
	
	public synchronized static void setExecutorService(PoolId<Runnable> poolId, ExecutorService es) {
		if(executorServiceByPoolId.containsKey(poolId)) {
			throw new AssertionError("ExecutorService should be initialized once for " + poolId);
		} else {
			executorServiceByPoolId.put(poolId, es);
		}
	}
	
	protected PoolId<T> poolId;
	protected A arg;
	
	public void execute(PoolId<T> poolId) {
		try {
			this.poolId = poolId;
			getExecutorService(poolId).execute(this);
		} catch(Exception e) {
			this.poolId = null;
			this.arg = null;
			Pools.get().getPool(EXECUTOR_POOL_ID).accept(this);
			throw e;
		}
	}
	
	@Override
	public void accept(A arg) {
		this.arg = arg;
	}
	
	@Override
	public void run() {
		Pools pools = Pools.get();
		try {
			Pool<T> pool = pools.getPool(poolId);
			T instance = pool.get();
			try {
				instance.accept(arg);
				instance.run();
			} finally {
				pool.accept(instance);
			}
		} finally {
			this.poolId = null;
			this.arg = null;
			pools.getPool(EXECUTOR_POOL_ID).accept(this);
		}
	}
	
}
