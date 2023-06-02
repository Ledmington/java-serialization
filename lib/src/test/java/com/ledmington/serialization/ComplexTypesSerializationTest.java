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

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class ComplexTypesSerializationTest {

    private Serializer ser;

    @BeforeEach
    public void setup() {
        ser = new Serializer();
    }

    @Test
    public void serializeEmptyOptional() {
        ser.write(Optional.empty());
        assertArrayEquals(new byte[] {ClassCodes.OPTIONAL.getCode(), 0x00}, ser.toByteArray());
    }

    @Test
    public void serializeOptional() {
        ser.write(Optional.of(15));
        assertArrayEquals(
                new byte[] {
                    ClassCodes.OPTIONAL.getCode(),
                    (byte) 0xff,
                    ClassCodes.INTEGER.getCode(),
                    0x00,
                    0x00,
                    0x00,
                    (byte) 0x0f
                },
                ser.toByteArray());
    }
}
