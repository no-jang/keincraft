package common.util.pool;

/**
 * Object which is resettable and so on can be used better by a pool
 *
 * @see Pool
 */
public interface Poolable {

    /**
     * Sets all variables to standard values
     */
    void reset();
}
