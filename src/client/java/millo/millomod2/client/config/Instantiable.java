package millo.millomod2.client.config;

public interface Instantiable<T extends ConfigValue<?> & Instantiable<T>> {
    T createNewInstance();
}
