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
		//������� ����� ������ ������ ������� ������� ����������� Releasable
		//...��� ����������� ��� ����z������� ������ �� ����������� ����� �������� ���.
		Foo foo = LocalPool.get(Foo.class);
		try {
			foo.run();
		} finally {
			foo.release();
		}		
	}
		
	
}
