package xyz.destr.factory;

import java.util.HashMap;

public class Factorys {
	
	protected static HashMap<FactorySignature<?>, Factory<?>> factoryBySignature = new HashMap<>();
	
	public static <T> Factory<T> get(Class<T> clazz, Object... args) {
		return get(FactorySignature.get(clazz, args));		
	}
	
	public static synchronized <T> Factory<T> get(FactorySignature<T> signature) {
		@SuppressWarnings("unchecked")
		final Factory<T> factory = (Factory<T>) factoryBySignature.get(signature);
		if(factory == null) {
			final Factory<T> newFactory = new BasicFactory<T>(signature);
			factoryBySignature.put(signature, newFactory);
			return newFactory;
		} else {
			return factory;
		}
	}
	
	public static synchronized void set(Factory<?> factory) {
		FactorySignature<?> signature = factory.getSignature();
		if(factoryBySignature.containsKey(signature)) {
			throw new AssertionError("Factory alrady exists " + signature);
		} else {
			factoryBySignature.put(signature, factory);
		}
	}
	
}
