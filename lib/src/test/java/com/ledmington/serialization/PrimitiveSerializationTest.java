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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class PrimitiveSerializationTest {

    private Serializer ser;

    @BeforeEach
    public void setup() {
        ser = new Serializer();
    }

    @Test
    public void writeBooleanPrimitive() {
        ser.write(true);
        ser.write(false);
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0xff, 0x00});
    }

    @Test
    public void writeBooleanBoxed() {
        ser.write(Boolean.TRUE);
        ser.write(Boolean.FALSE);
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0xff, 0x00});
    }

    @Test
    public void writeBytePrimitive() {
        ser.write((byte) 99);
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0x63});
    }

    @Test
    public void writeByteBoxed() {
        ser.write(Byte.valueOf((byte) 99));
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0x63});
    }

    @Test
    public void writeShortPrimitive() {
        ser.write((short) 1234);
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0x04, (byte) 0xd2});
    }

    @Test
    public void writeShortBoxed() {
        ser.write(Short.valueOf((short) 1234));
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0x04, (byte) 0xd2});
    }

    @Test
    public void writeIntPrimitive() {
        ser.write(123_456_789);
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0x07, (byte) 0x5b, (byte) 0xcd, (byte) 0x15});
    }

    @Test
    public void writeIntegerBoxed() {
        ser.write(Integer.valueOf(123_456_789));
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0x07, (byte) 0x5b, (byte) 0xcd, (byte) 0x15});
    }

    @Test
    public void writeLongPrimitive() {
        ser.write(1_234_567_890_123_456_789L);
        assertArrayEquals(ser.toByteArray(), new byte[] {
            (byte) 0x11, (byte) 0x22, (byte) 0x10, (byte) 0xf4, (byte) 0x7d, (byte) 0xe9, (byte) 0x81, (byte) 0x15
        });
    }

    @Test
    public void writeLongBoxed() {
        ser.write(Long.valueOf(1_234_567_890_123_456_789L));
        assertArrayEquals(ser.toByteArray(), new byte[] {
            (byte) 0x11, (byte) 0x22, (byte) 0x10, (byte) 0xf4, (byte) 0x7d, (byte) 0xe9, (byte) 0x81, (byte) 0x15
        });
    }

    @Test
    public void writeFloatPrimitive() {
        ser.write((float) 12.34);
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0x41, (byte) 0x45, (byte) 0x70, (byte) 0xa4});
    }

    @Test
    public void writeFloatBoxed() {
        ser.write(Float.valueOf((float) 12.34));
        assertArrayEquals(ser.toByteArray(), new byte[] {(byte) 0x41, (byte) 0x45, (byte) 0x70, (byte) 0xa4});
    }

    @Test
    public void writeDoublePrimitive() {
        ser.write(1234.5678);
        assertArrayEquals(ser.toByteArray(), new byte[] {
            (byte) 0x40, (byte) 0x93, (byte) 0x4A, (byte) 0x45, (byte) 0x6D, (byte) 0x5C, (byte) 0xFA, (byte) 0xAD
        });
    }

    @Test
    public void writeDoubleBoxed() {
        ser.write(Double.valueOf(1234.5678));
        assertArrayEquals(ser.toByteArray(), new byte[] {
            (byte) 0x40, (byte) 0x93, (byte) 0x4A, (byte) 0x45, (byte) 0x6D, (byte) 0x5C, (byte) 0xFA, (byte) 0xAD
        });
    }
}
