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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

enum ClassCodes {
    BYTE((byte) 1, Byte.class),
    BOOLEAN((byte) 2, Boolean.class),
    SHORT((byte) 3, Short.class),
    INTEGER((byte) 4, Integer.class),
    LONG((byte) 5, Long.class),
    FLOAT((byte) 6, Float.class),
    DOUBLE((byte) 7, Double.class),
    CHAR((byte) 8, Character.class),
    STRING((byte) 9, String.class),
    OPTIONAL((byte) 10, Optional.class),
    LIST((byte) 11, List.class),
    SET((byte) 12, Set.class),
    MAP((byte) 13, Map.class),
    ARRAY((byte) 14, Object[].class);

    private static final Map<Byte, Class<?>> codeToClass = new HashMap<>();
    private static final Map<Class<?>, Byte> classToCode = new HashMap<>();

    private static void buildInnerMaps() {
        for (ClassCodes c : ClassCodes.values()) {
            if (codeToClass.containsKey(c.code)) {
                throw new IllegalStateException(String.format("Duplicate code %d", c.code));
            }
            if (classToCode.containsKey(c.clazz)) {
                throw new IllegalStateException(String.format("Duplicate class %s", c.clazz.getName()));
            }
            codeToClass.put(c.code, c.clazz);
            classToCode.put(c.clazz, c.code);
        }
    }

    public static Class<?> fromCode(byte code) {
        if (codeToClass.isEmpty()) {
            buildInnerMaps();
        }
        return codeToClass.get(code);
    }

    public static byte fromClass(final Class<?> clazz) {
        if (classToCode.isEmpty()) {
            buildInnerMaps();
        }
        return classToCode.get(clazz);
    }

    private final byte code;
    private final Class<?> clazz;

    ClassCodes(byte code, final Class<?> clazz) {
        this.code = code;
        this.clazz = clazz;
    }

    public byte getCode() {
        return code;
    }

    public Class<?> getRepresentedClass() {
        return clazz;
    }
}
