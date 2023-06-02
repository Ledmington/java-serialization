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

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public final class Serializer {
    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();
    private final Map<Class<?>, Consumer<Object>> serializers = new HashMap<>();

    public Serializer() {
        serializers.put(Boolean.class, obj -> write(((Boolean) obj).equals(true)));
        serializers.put(Byte.class, obj -> write(((Byte) obj).byteValue()));
        serializers.put(Short.class, obj -> write(((Short) obj).shortValue()));
        serializers.put(Integer.class, obj -> write(((Integer) obj).intValue()));
        serializers.put(Long.class, obj -> write(((Long) obj).longValue()));
        serializers.put(Float.class, obj -> write(((Float) obj).floatValue()));
        serializers.put(Double.class, obj -> write(((Double) obj).doubleValue()));

        serializers.put(Optional.class, obj -> {
            writeRaw(ClassCodes.OPTIONAL.getCode());
            final Optional<?> opt = (Optional<?>) obj;
            if (opt.isEmpty()) {
                writeRaw((byte) 0x00);
            } else {
                writeRaw((byte) 0xff);
                write(opt.orElseThrow());
            }
        });
    }

    public byte[] toByteArray() {
        // here we do not flush and/or close the stream since
        // it has no effect
        return baos.toByteArray();
    }

    private void writeRaw(byte b) {
        baos.write(b);
    }

    public void write(byte b) {
        writeRaw(ClassCodes.BYTE.getCode());
        writeRaw(b);
    }

    public void write(boolean b) {
        writeRaw(ClassCodes.BOOLEAN.getCode());
        if (b) {
            writeRaw((byte) 0xff);
        } else {
            writeRaw((byte) 0x00);
        }
    }

    public void write(short s) {
        writeRaw(ClassCodes.SHORT.getCode());
        writeRaw((byte) (s >> 8));
        writeRaw((byte) (s & 0xff));
    }

    public void write(int i) {
        writeRaw(ClassCodes.INTEGER.getCode());
        writeRaw((byte) (i >> 24));
        writeRaw((byte) ((i >> 16) & 0xff));
        writeRaw((byte) ((i >> 8) & 0xff));
        writeRaw((byte) (i & 0xff));
    }

    public void write(long l) {
        writeRaw(ClassCodes.LONG.getCode());
        writeRaw((byte) (l >> 56));
        writeRaw((byte) ((l >> 48) & 0xff));
        writeRaw((byte) ((l >> 40) & 0xff));
        writeRaw((byte) ((l >> 32) & 0xff));
        writeRaw((byte) ((l >> 24) & 0xff));
        writeRaw((byte) ((l >> 16) & 0xff));
        writeRaw((byte) ((l >> 8) & 0xff));
        writeRaw((byte) (l & 0xff));
    }

    public void write(float f) {
        writeRaw(ClassCodes.FLOAT.getCode());
        int i = Float.floatToIntBits(f);
        writeRaw((byte) (i >> 24));
        writeRaw((byte) ((i >> 16) & 0xff));
        writeRaw((byte) ((i >> 8) & 0xff));
        writeRaw((byte) (i & 0xff));
    }

    public void write(double d) {
        writeRaw(ClassCodes.DOUBLE.getCode());
        long l = Double.doubleToLongBits(d);
        writeRaw((byte) (l >> 56));
        writeRaw((byte) ((l >> 48) & 0xff));
        writeRaw((byte) ((l >> 40) & 0xff));
        writeRaw((byte) ((l >> 32) & 0xff));
        writeRaw((byte) ((l >> 24) & 0xff));
        writeRaw((byte) ((l >> 16) & 0xff));
        writeRaw((byte) ((l >> 8) & 0xff));
        writeRaw((byte) (l & 0xff));
    }

    public void write(final Object obj) {
        if (!serializers.containsKey(obj.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "Cannot serialize object with unknown class %s",
                    obj.getClass().getName()));
        }
        serializers.get(obj.getClass()).accept(obj);
    }
}
