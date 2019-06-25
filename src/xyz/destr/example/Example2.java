package xyz.destr.example;

import xyz.destr.pool.LocalPool;
import xyz.destr.pool.PoolId;
import xyz.destr.pool.functions.Releasable;

public class Example2 {
	
	public static class Foo implements Releasable<Foo>, Runnable {

		static final PoolId<Foo> POOL_ID = PoolId.get(Foo.class);
		
		@Override
		public PoolId<Foo> getPoolId() {
			return POOL_ID;
		}

		@Override
		public void release() {
			LocalPool.accept(POOL_ID, this);
		}

		@Override
		public void run() {
			System.out.println("Foo run");
		}
		
	}
	
	public static void main(String[] args) {
		//Ленивый гетер выдает только объекты классов расширяющих Releasable
		//...что гарантирует что польzователю класса не потребуется снова получать пул.
		Foo foo = LocalPool.get(Foo.class);
		try {
			foo.run();
		} finally {
			foo.release();
		}		
	}
		
	
}
