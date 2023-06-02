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
        serializers.put(Character.class, obj -> write(((Character) obj).charValue()));

        serializers.put(Optional.class, obj -> {
            final Optional<?> opt = (Optional<?>) obj;
            if (opt.isEmpty()) {
                writeRaw((byte) 0x00);
            } else {
                writeRaw((byte) 0xff);
                write(opt.orElseThrow());
            }
        });

        serializers.put(String.class, obj -> {
            final String s = (String) obj;
            write(s.length());
            for (int i = 0; i < s.length(); i++) {
                write(s.charAt(i));
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
        writeRaw(b);
    }

    public void write(boolean b) {
        if (b) {
            writeRaw((byte) 0xff);
        } else {
            writeRaw((byte) 0x00);
        }
    }

    public void write(short s) {
        writeRaw((byte) (s >> 8));
        writeRaw((byte) (s & 0xff));
    }

    public void write(int i) {
        writeRaw((byte) (i >> 24));
        writeRaw((byte) ((i >> 16) & 0xff));
        writeRaw((byte) ((i >> 8) & 0xff));
        writeRaw((byte) (i & 0xff));
    }

    public void write(long l) {
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
        int i = Float.floatToIntBits(f);
        writeRaw((byte) (i >> 24));
        writeRaw((byte) ((i >> 16) & 0xff));
        writeRaw((byte) ((i >> 8) & 0xff));
        writeRaw((byte) (i & 0xff));
    }

    public void write(double d) {
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

    public void write(char c) {
        writeRaw((byte) (c >> 8));
        writeRaw((byte) (c & 0xff));
    }

    public void write(final Object obj) {
        final Class<?> clazz = obj.getClass();
        if (!serializers.containsKey(obj.getClass())) {
            throw new IllegalArgumentException(String.format(
                    "Cannot serialize object with unknown class %s",
                    obj.getClass().getName()));
        }
        final byte classCode = ClassCodes.fromClass(clazz);
        writeRaw(classCode);
        serializers.get(obj.getClass()).accept(obj);
    }
}
