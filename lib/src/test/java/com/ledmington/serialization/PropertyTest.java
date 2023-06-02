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

import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public final class PropertyTest {
    private static final Random rnd = new Random();

    private static Object randomObject() {
        final List<Supplier<Object>> sup = List.of(
                rnd::nextBoolean,
                () -> (byte) rnd.nextInt(),
                () -> (short) rnd.nextInt(),
                rnd::nextInt,
                rnd::nextLong,
                rnd::nextFloat,
                rnd::nextDouble,
                () -> (char) rnd.nextInt(),
                () -> Optional.of(randomObject()),
                // random string
                () -> {
                    final StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < rnd.nextInt() % 10; i++) {
                        sb.append((char) rnd.nextInt());
                    }
                    return sb.toString();
                });
        return sup.get(rnd.nextInt(sup.size() - 1)).get();
    }

    private static Stream<Arguments> randomValues() {
        return Stream.concat(
                Stream.of(
                        Arguments.of(true),
                        Arguments.of(false),
                        Arguments.of(Byte.MAX_VALUE),
                        Arguments.of(Byte.MIN_VALUE),
                        Arguments.of(Short.MAX_VALUE),
                        Arguments.of(Short.MIN_VALUE),
                        Arguments.of(Integer.MAX_VALUE),
                        Arguments.of(Integer.MIN_VALUE),
                        Arguments.of(Long.MAX_VALUE),
                        Arguments.of(Long.MIN_VALUE),
                        Arguments.of(Float.MAX_VALUE),
                        Arguments.of(Float.MIN_VALUE),
                        Arguments.of(Double.MAX_VALUE),
                        Arguments.of(Double.MIN_VALUE),
                        Arguments.of(Optional.empty())),
                Stream.generate(() -> Arguments.of(randomObject())).distinct().limit(100));
    }

    @ParameterizedTest
    @MethodSource("randomValues")
    public void eachObjectShouldBeEqualToTheDeserializedOne(final Object obj) {
        final Serializer ser = new Serializer();
        ser.write(obj);
        final Deserializer des = new Deserializer(ser.toByteArray());
        final Object deserialized = des.read();
        assertEquals(obj, deserialized);
        assertEquals(deserialized, obj);
    }
}
