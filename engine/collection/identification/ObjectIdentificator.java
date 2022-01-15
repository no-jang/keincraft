package engine.collection.identification;

public class ObjectIdentificator implements Identificator<Object> {
    @Override
    public boolean isIdentifiable(Object object) {
        return true;
    }

    @Override
    public boolean equals(Object object1, Object object2) {
        return object1.equals(object2);
    }

    @Override
    public int hashCode(Object object) {
        return object.hashCode();
    }

    @Override
    public boolean equals(Object object) {
        if (object == null) {
            return false;
        }

        return object.getClass() == this.getClass();
    }

    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
}
