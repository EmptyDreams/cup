package java_cup.runtime.symbol.complex;

import java.util.Objects;

public class ComplexObjectSymbol extends ComplexSymbol {

    private final Object value;

    public ComplexObjectSymbol(int id, Location left, Location right, Object value) {
        super(id, left, right);
        this.value = value;
    }

    public ComplexObjectSymbol(int id, Object value) {
        super(id);
        this.value = value;
    }

    @Override
    public boolean isNull() {
        return value == null;
    }

    @Override
    public <T> T value() {
        //noinspection unchecked
        return (T) this.value;
    }

    @Override
    public byte getAsByte() {
        if (value instanceof Byte) {
            return (Byte) value;
        }
        return super.getAsByte();
    }

    @Override
    public short getAsShort() {
        if (value instanceof Short) {
            return (Short) value;
        }
        return super.getAsShort();
    }

    @Override
    public int getAsInt() {
        if (value instanceof Integer) {
            return (Integer) value;
        }
        return super.getAsInt();
    }

    @Override
    public long getAsLong() {
        if (value instanceof Long) {
            return (Long) value;
        }
        return super.getAsLong();
    }

    @Override
    public float getAsFloat() {
        if (value instanceof Float) {
            return (Float) value;
        }
        return super.getAsFloat();
    }

    @Override
    public double getAsDouble() {
        if (value instanceof Double) {
            return (Double) value;
        }
        return super.getAsDouble();
    }

    @Override
    public boolean getAsBoolean() {
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return super.getAsBoolean();
    }

    @Override
    public char getAsChar() {
        if (value instanceof Character) {
            return (Character) value;
        }
        return super.getAsChar();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value) + super.hashCode() * 31;
    }

    @Override
    public String toString() {
        return "complex object #" + sym + ", value=[" + value() + "], pos=[" + getLeft() + " / " + getRight() + "]";
    }
}