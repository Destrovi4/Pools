package xyz.destr.factory;

public abstract class AbstractFactory<T> implements Factory<T> {

	protected final FactorySignature<T> signature;
	
	public AbstractFactory(FactorySignature<T> signature) {
		this.signature = signature;
	}
	
	public FactorySignature<T> getSignature() {
		return signature;
	}

}
