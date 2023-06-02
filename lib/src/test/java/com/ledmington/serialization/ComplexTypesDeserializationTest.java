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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

public final class ComplexTypesDeserializationTest {
    @Test
    public void deserializeEmptyOptional() {
        final Deserializer des = new Deserializer(new byte[] {ClassCodes.OPTIONAL.getCode(), 0x00});
        assertEquals(Optional.empty(), des.read());
    }

    @Test
    public void deserializeOptional() {
        final Deserializer des = new Deserializer(new byte[] {
            ClassCodes.OPTIONAL.getCode(), (byte) 0xff, ClassCodes.INTEGER.getCode(), 0x00, 0x00, 0x00, (byte) 0x0f
        });
        assertEquals(Optional.of(15), des.read());
    }

    @Test
    public void deserializeWrongOptional() {
        final Deserializer des = new Deserializer(new byte[] {ClassCodes.OPTIONAL.getCode(), (byte) 0x15});
        assertThrows(InvalidOptionalException.class, des::read);
    }

    @Test
    public void deserializeEmptyString() {
        final Deserializer des = new Deserializer(new byte[] {ClassCodes.STRING.getCode(), 0x00, 0x00, 0x00, 0x00});
        assertEquals("", des.read());
    }

    @Test
    public void deserializeString() {
        final Deserializer des = new Deserializer(new byte[] {
            ClassCodes.STRING.getCode(),
            0x00,
            0x00,
            0x00,
            (byte) 0x0f,
            (byte) 0x00,
            (byte) 0x73,
            (byte) 0x00,
            (byte) 0x65,
            (byte) 0x00,
            (byte) 0x72,
            (byte) 0x00,
            (byte) 0x69,
            (byte) 0x00,
            (byte) 0x61,
            (byte) 0x00,
            (byte) 0x6c,
            (byte) 0x00,
            (byte) 0x69,
            (byte) 0x00,
            (byte) 0x7a,
            (byte) 0x00,
            (byte) 0x65,
            (byte) 0x00,
            (byte) 0x53,
            (byte) 0x00,
            (byte) 0x74,
            (byte) 0x00,
            (byte) 0x72,
            (byte) 0x00,
            (byte) 0x69,
            (byte) 0x00,
            (byte) 0x6e,
            (byte) 0x00,
            (byte) 0x67
        });
        assertEquals("serializeString", des.read());
    }
}
