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

import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public final class CommonDeserializationTest {
    @Test
    public void readNullArray() {
        assertThrows(NullPointerException.class, () -> new Deserializer(null));
    }

    @Test
    public void readEmptyArray() {
        assertThrows(IllegalArgumentException.class, () -> new Deserializer(new byte[0]));
    }

    @Test
    public void terminatedStream() {
        final Deserializer des = new Deserializer(new byte[] {0x00});
        des.readByte();
        assertThrows(IllegalStateException.class, () -> des.readByte());
    }

    @Test
    public void cantDeserializeObject() {
        final Deserializer des = new Deserializer(new byte[] {0x00, 0x00});
        assertThrows(IllegalArgumentException.class, () -> des.read(Object.class));
    }
}
