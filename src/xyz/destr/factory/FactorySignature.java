package xyz.destr.factory;

import java.util.Arrays;
import java.util.HashMap;

public class FactorySignature<T> {
	
	protected static HashMap<Object, FactorySignature<?>> cash = new HashMap<>();
	
	protected Class<T> clazz;
	protected Object[] args;
	protected final int hash;
	
	private FactorySignature(Class<T> clazz, Object[] args) {
		this.clazz = clazz;
		this.args = args;
		this.hash = signatureHash(clazz, args);
	}
	
	@Override
	public int hashCode() {
		return hash;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object other) {
		if(other instanceof FactorySignature) {
			FactorySignature<?> otherSignature = (FactorySignature<?>)other;
			if(otherSignature.clazz.equals(clazz)) {
				return equals((FactorySignature<T>)otherSignature);
			} else {
				return false;
			}
		}
		return false;
	}
	
	public boolean equals(FactorySignature<T> other) {
		return Arrays.equals(args, other.args);
	}
	
	public Class<T> produces() {
		return clazz;
	}
	
	private static int signatureHash(Class<?> clazz, Object... args) {
		int hash = 37;
		hash += 37 * hash + clazz.hashCode();
		if(args == null) {
			return hash;
		}
		for(Object arg: args) {
			hash += 37 * hash + arg.hashCode();
		}
		return hash;
	}
	
	public static synchronized <T> FactorySignature<T> get(Class<T> clazz, Object... args){
		FactorySignature<T> signature = new FactorySignature<T>(clazz, args);
		@SuppressWarnings("unchecked")
		FactorySignature<T> cashed = (FactorySignature<T>)cash.get(signature);
		if(cashed == null) {
			cash.put(signature, signature);
			return signature;
		} else {
			return cashed;
		}
	}
}
