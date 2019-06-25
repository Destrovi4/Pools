package xyz.destr.pool.functions;

import xyz.destr.pool.PoolId;

public interface Releasable<T extends Releasable<T>> {
	
	public PoolId<T> getPoolId();
	
	public void release();
	
}