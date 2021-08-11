package stami.ray.ariba;

import javax.naming.ServiceUnavailableException;

public interface CBService1 {
    public abstract Integer callService();
    public abstract void setName(String name);
    public abstract String getName();
}
