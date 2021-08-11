package stami.ray.ariba;

import javax.naming.ServiceUnavailableException;

public interface CBService {
    public abstract Integer callService();
    public abstract Integer fallBack(Throwable T);
    public abstract void setName(String name);
    public abstract String getName();
}
