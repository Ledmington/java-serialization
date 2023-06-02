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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public final class CommonSerializationTest {

    private Serializer ser;

    @BeforeEach
    public void setup() {
        ser = new Serializer();
    }

    @Test
    public void initiallyEmpty() {
        assertArrayEquals(ser.toByteArray(), new byte[0]);
    }

    @Test
    public void cantSerializeNull() {
        assertThrows(NullPointerException.class, () -> ser.write(null));
    }

    @Test
    public void cantSerializeObject() {
        assertThrows(IllegalArgumentException.class, () -> ser.write(new Object()));
    }
}
