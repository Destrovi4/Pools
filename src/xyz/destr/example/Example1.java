package xyz.destr.example;

import java.util.concurrent.ArrayBlockingQueue;

import xyz.destr.pool.LocalPool;
import xyz.destr.pool.Pool;
import xyz.destr.pool.PoolId;

public class Example1 {

	public static final int COUNT = 100;
	
	public static class Foo {
		public int i;
		public Foo set(int i) {
			this.i = i;
			return this;
		}
	}
	
	public static final PoolId<Foo> poolId = PoolId.get(Foo.class);
	
	public static final ArrayBlockingQueue<Foo> queue = new ArrayBlockingQueue<Foo>(3);
	
	public static void main(String[] args) {
		new Thread(Example1::source).start();
		new Thread(Example1::release).start();
	}
	
	public static void source() {
		Pool<Foo> pool = LocalPool.getPool(poolId);
		try {
			for(int i = 0; i < COUNT; i++) {
				queue.put(pool.get().set(i));
				Thread.sleep(1000);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void release() {
		Pool<Foo> pool = LocalPool.getPool(poolId);
		try {
			for(int i = 0; i < COUNT; i++) {
				Foo foo = queue.take();
				System.out.println(foo.i);
				pool.accept(foo);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
