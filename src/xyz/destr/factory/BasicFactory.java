package xyz.destr.factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

public class BasicFactory<T> extends AbstractFactory<T> {

	protected final Supplier<T> constructor;
	
	public BasicFactory(FactorySignature<T> signature) {
		super(signature);
		if(signature.args == null || signature.args.length == 0) {
			constructor = new EmptyConstructor<T>(signature.clazz);
		} else {
			constructor = new ConstructorWithArguments<T>(signature.clazz, signature.args);
		}
	}
	
	@Override
	public T get() {
		return constructor.get();
	}
	
	static class EmptyConstructor<T> implements Supplier<T> {

		protected Class<T> clazz;
		
		public EmptyConstructor(Class<T> clazz) {
			this.clazz = clazz;
		}
		
		@Override
		public T get() {
			try {
				return clazz.newInstance();
			} catch (InstantiationException | IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	static class ConstructorWithArguments<T> implements Supplier<T> {

		protected Constructor<T> constructor;
		protected Object[] args;
		
		public ConstructorWithArguments(Class<T> clazz, Object... args) {
			Class<?>[] classes = new Class[args.length];
			for(int i = 0; i < args.length; i++) {
				classes[i] = args[i].getClass();
			}
			try {
				this.constructor = clazz.getConstructor(classes);
				constructor.setAccessible(true);
			} catch (NoSuchMethodException | SecurityException e) {
				throw new RuntimeException(e);
			}
			this.args = args;
		}
		
		@Override
		public T get() {
			try {
				return constructor.newInstance(args);
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
				throw new RuntimeException(e);
			}
		}
	}
}