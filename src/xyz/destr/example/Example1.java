package xyz.destr.example;

import java.util.function.Consumer;

import xyz.destr.pool.LocalPool;

public class Example1 {

	public static class Foo implements Runnable, Consumer<String> {

		protected String text;
		
		@Override
		public void run() {
			System.out.println("Foo! " + text);
			this.text = null;
			System.exit(0);
		}

		@Override
		public void accept(String text) {
			this.text = text;
		}
		
	}
	
	public static void main(String[] args) {
		LocalPool.execute("Hello world", Foo.class);
	}

}
