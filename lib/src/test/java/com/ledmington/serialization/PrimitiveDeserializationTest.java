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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public final class PrimitiveDeserializationTest {
    @Test
    public void readBooleanPrimitive() {
        final Deserializer des = new Deserializer(new byte[] {(byte) 0xff, (byte) 0x00});
        assertTrue(des.readBoolean());
        assertFalse(des.readBoolean());
    }

    @Test
    public void readBooleanBoxed() {
        final Deserializer des = new Deserializer(
                new byte[] {ClassCodes.BOOLEAN.getCode(), (byte) 0xff, ClassCodes.BOOLEAN.getCode(), (byte) 0x00});
        assertEquals(true, des.read());
        assertEquals(false, des.read());
    }

    @Test
    public void readWrongBoolean() {
        final Deserializer des = new Deserializer(new byte[] {ClassCodes.BOOLEAN.getCode(), (byte) 0x12});
        assertThrows(InvalidBooleanException.class, des::readBoolean);
    }

    @Test
    public void readBytePrimitive() {
        final Deserializer des = new Deserializer(new byte[] {(byte) 0x12, (byte) 0x34});
        assertEquals(des.readByte(), (byte) 0x12);
        assertEquals(des.readByte(), (byte) 0x34);
    }

    @Test
    public void readByteBoxed() {
        final Deserializer des = new Deserializer(
                new byte[] {ClassCodes.BYTE.getCode(), (byte) 0x12, ClassCodes.BYTE.getCode(), (byte) 0x34});
        assertEquals((byte) 0x12, des.read());
        assertEquals((byte) 0x34, des.read());
    }

    @Test
    public void readShortPrimitive() {
        final Deserializer des = new Deserializer(new byte[] {(byte) 0x12, (byte) 0x34});
        assertEquals(des.readShort(), (short) 4660);
    }

    @Test
    public void readShortBoxed() {
        final Deserializer des = new Deserializer(new byte[] {ClassCodes.SHORT.getCode(), (byte) 0x12, (byte) 0x34});
        assertEquals((short) 4660, des.read());
    }

    @Test
    public void readIntegerPrimitive() {
        final Deserializer des = new Deserializer(new byte[] {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78});
        assertEquals(des.readInt(), 305_419_896);
    }

    @Test
    public void readIntegerBoxed() {
        final Deserializer des = new Deserializer(
                new byte[] {ClassCodes.INTEGER.getCode(), (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78});
        assertEquals(305_419_896, des.read());
    }

    @Test
    public void readLongPrimitive() {
        final Deserializer des = new Deserializer(new byte[] {
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0x12, (byte) 0x34, (byte) 0x56
        });
        assertEquals(des.readLong(), 1_311_768_467_284_833_366L);
    }

    @Test
    public void readLongBoxed() {
        final Deserializer des = new Deserializer(new byte[] {
            ClassCodes.LONG.getCode(),
            (byte) 0x12,
            (byte) 0x34,
            (byte) 0x56,
            (byte) 0x78,
            (byte) 0x90,
            (byte) 0x12,
            (byte) 0x34,
            (byte) 0x56
        });
        assertEquals(1_311_768_467_284_833_366L, des.read());
    }

    @Test
    public void readFloatPrimitive() {
        final Deserializer des = new Deserializer(new byte[] {(byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78});
        assertEquals(des.readFloat(), (float) 5.6904566139e-28);
    }

    @Test
    public void readFloatBoxed() {
        final Deserializer des = new Deserializer(
                new byte[] {ClassCodes.FLOAT.getCode(), (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78});
        assertEquals((float) 5.6904566139e-28, des.read());
    }

    @Test
    public void readDoublePrimitive() {
        final Deserializer des = new Deserializer(new byte[] {
            (byte) 0x12, (byte) 0x34, (byte) 0x56, (byte) 0x78, (byte) 0x90, (byte) 0x12, (byte) 0x34, (byte) 0x56
        });
        assertEquals(des.readDouble(), 5.62634909901491201382066931077E-221);
    }

    @Test
    public void readDoubleBoxed() {
        final Deserializer des = new Deserializer(new byte[] {
            ClassCodes.DOUBLE.getCode(),
            (byte) 0x12,
            (byte) 0x34,
            (byte) 0x56,
            (byte) 0x78,
            (byte) 0x90,
            (byte) 0x12,
            (byte) 0x34,
            (byte) 0x56
        });
        assertEquals(5.62634909901491201382066931077E-221, des.read());
    }
}
