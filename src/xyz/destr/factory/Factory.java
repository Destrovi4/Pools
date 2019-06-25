package xyz.destr.factory;

import java.util.function.Supplier;

public interface Factory<T> extends Supplier<T> {
	
	public FactorySignature<T> getSignature();
		
}
