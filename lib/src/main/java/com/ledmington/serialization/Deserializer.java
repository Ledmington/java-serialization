/*
* java-serialization - A simple, efficient and fast serialization library.
* Copyright (C) 2023-2023 Filippo Barbari <filippo.barbari@gmail.com>
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package com.ledmington.serialization;

import java.io.ByteArrayInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

public final class Deserializer {
    private final ByteArrayInputStream bais;
    private final Map<Class<?>, Supplier<Object>> deserializers = new HashMap<>();

    public Deserializer(final byte[] input) {
        Objects.requireNonNull(input);
        if (input.length == 0) {
            throw new IllegalArgumentException("Input byte array can't be empty.");
        }
        this.bais = new ByteArrayInputStream(input);

        deserializers.put(Boolean.class, () -> readBoolean());
        deserializers.put(Byte.class, () -> readByte());
        deserializers.put(Short.class, () -> readShort());
        deserializers.put(Integer.class, () -> readInt());
        deserializers.put(Long.class, () -> readLong());
        deserializers.put(Float.class, () -> readFloat());
        deserializers.put(Double.class, () -> readDouble());
    }

    private byte readRaw() {
        int b = bais.read();
        if (b == -1) {
            throw new IllegalStateException("Can't read if the stream is terminated.");
        }
        return (byte) (b & 0xff);
    }

    public byte readByte() {
        final byte code = readRaw();
        if (code != ClassCodes.BYTE.getCode()) {
            throw new InvalidByteException(code);
        }
        return readRaw();
    }

    public boolean readBoolean() {
        byte b = readByte();
        if (b == (byte) 0x00) {
            return false;
        }
        if (b == (byte) 0xff) {
            return true;
        }
        throw new InvalidBooleanException(b);
    }

    public short readShort() {
        short s = (short) 0x0;
        s = (short) ((s << 8) | (readByte() & 0xff));
        s = (short) ((s << 8) | (readByte() & 0xff));
        return s;
    }

    public int readInt() {
        int i = 0x0;
        i = (i << 8) | (readByte() & 0xff);
        i = (i << 8) | (readByte() & 0xff);
        i = (i << 8) | (readByte() & 0xff);
        i = (i << 8) | (readByte() & 0xff);
        return i;
    }

    public long readLong() {
        long l = (long) 0x0;
        l = (l << 8) | (readByte() & 0xff);
        l = (l << 8) | (readByte() & 0xff);
        l = (l << 8) | (readByte() & 0xff);
        l = (l << 8) | (readByte() & 0xff);
        l = (l << 8) | (readByte() & 0xff);
        l = (l << 8) | (readByte() & 0xff);
        l = (l << 8) | (readByte() & 0xff);
        l = (l << 8) | (readByte() & 0xff);
        return l;
    }

    public float readFloat() {
        return Float.intBitsToFloat(readInt());
    }

    public double readDouble() {
        return Double.longBitsToDouble(readLong());
    }

    public Object read() {
        final byte classCode = readByte();
        final Class<?> clazz = ClassCodes.fromCode(classCode);
        if (!deserializers.containsKey(clazz)) {
            throw new IllegalArgumentException(
                    String.format("No registered deserializer for the given class %s", clazz.getName()));
        }
        return deserializers.get(clazz).get();
    }
}
