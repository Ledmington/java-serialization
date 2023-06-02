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
import java.util.Objects;

public final class Deserializer {
    private final ByteArrayInputStream bais;

    public Deserializer(final byte[] input) {
        Objects.requireNonNull(input);
        if (input.length == 0) {
            throw new IllegalArgumentException("Input byte array can't be empty.");
        }
        this.bais = new ByteArrayInputStream(input);
    }

    public byte readByte() {
        int b = bais.read();
        if (b == -1) {
            throw new IllegalStateException("Can't read if the stream is terminated.");
        }
        return (byte) (b & 0xff);
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
        return (short) (readByte() << 8 | readByte());
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
}
