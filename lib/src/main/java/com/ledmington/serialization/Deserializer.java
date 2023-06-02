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
import java.util.Optional;
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

        deserializers.put(Boolean.class, this::readBoolean);
        deserializers.put(Byte.class, this::readByte);
        deserializers.put(Short.class, this::readShort);
        deserializers.put(Integer.class, this::readInt);
        deserializers.put(Long.class, this::readLong);
        deserializers.put(Float.class, this::readFloat);
        deserializers.put(Double.class, this::readDouble);
        deserializers.put(Character.class, this::readChar);

        deserializers.put(Optional.class, () -> {
            final byte b = readRaw();
            if (b == 0x00) {
                return Optional.empty();
            }
            if (b == (byte) 0xff) {
                return Optional.of(read());
            }
            throw new InvalidOptionalException(b);
        });

        deserializers.put(String.class, () -> {
            final StringBuilder sb = new StringBuilder();
            final int length = readInt();
            for (int i = 0; i < length; i++) {
                sb.append(readChar());
            }
            return sb.toString();
        });
    }

    private byte readRaw() {
        int b = bais.read();
        if (b == -1) {
            throw new IllegalStateException("Can't read if the stream is terminated.");
        }
        return (byte) (b & 0xff);
    }

    public byte readByte() {
        return readRaw();
    }

    public boolean readBoolean() {
        final byte b = readRaw();
        if (b == (byte) 0x00) {
            return false;
        }
        if (b == (byte) 0xff) {
            return true;
        }
        throw new InvalidBooleanException(b);
    }

    public short readShort() {
        short s = (short) (readRaw() & 0xff);
        s = (short) ((s << 8) | (readRaw() & 0xff));
        return s;
    }

    public int readInt() {
        int i = (readRaw() & 0xff);
        i = (i << 8) | (readRaw() & 0xff);
        i = (i << 8) | (readRaw() & 0xff);
        i = (i << 8) | (readRaw() & 0xff);
        return i;
    }

    public long readLong() {
        long l = (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        return l;
    }

    public float readFloat() {
        int i = (readRaw() & 0xff);
        i = (i << 8) | (readRaw() & 0xff);
        i = (i << 8) | (readRaw() & 0xff);
        i = (i << 8) | (readRaw() & 0xff);
        return Float.intBitsToFloat(i);
    }

    public double readDouble() {
        long l = (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        l = (l << 8) | (readRaw() & 0xff);
        return Double.longBitsToDouble(l);
    }

    public char readChar() {
        int c = (readRaw() & 0xff);
        c = (c << 8) | (readRaw() & 0xff);
        return (char) c;
    }

    public Object read() {
        final byte classCode = readRaw();
        final Class<?> clazz = ClassCodes.fromCode(classCode);
        if (!deserializers.containsKey(clazz)) {
            throw new IllegalArgumentException(
                    String.format("No registered deserializer for the given class %s", clazz.getName()));
        }
        return deserializers.get(clazz).get();
    }
}
