/*
 * Copyright 2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.littletreasures.examples;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.junit.jupiter.api.Test;

import org.springframework.util.function.ThrowingBiFunction;
import org.springframework.util.function.ThrowingConsumer;
import org.springframework.util.function.ThrowingFunction;
import org.springframework.util.function.ThrowingSupplier;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatRuntimeException;

/**
 * Example of the {@code Throwing...} functional interfaces in Spring.
 *
 * @see ThrowingConsumer
 * @see ThrowingSupplier
 * @see ThrowingFunction
 * @see ThrowingBiFunction
 */
public class ThrowingFunctionalInterfaceExampleTests {

	@Test
	void wrapsException() {
		Consumer<String> consumer = ThrowingConsumer.of(this::throwIOException);
		assertThatRuntimeException().isThrownBy(() -> consumer.accept("hello")).withCauseInstanceOf(IOException.class);
	}

	@Test
	void wrapsWithDifferentException() {
		Consumer<String> consumer = ThrowingConsumer.of(this::throwIOException).throwing(IllegalStateException::new);
		assertThatIllegalStateException().isThrownBy(() -> consumer.accept("hello"))
			.withCauseInstanceOf(IOException.class);
	}

	@Test
	void wrapsWithDifferentExceptionAlternative() {
		Consumer<String> consumer = ThrowingConsumer.of(this::throwIOException, IllegalStateException::new);
		assertThatIllegalStateException().isThrownBy(() -> consumer.accept("hello"))
			.withCauseInstanceOf(IOException.class);
	}

	@Test
	void wrapsExceptionAtTimeOfCall() {
		ThrowingConsumer<String> consumer = ThrowingConsumer.of(this::throwIOException, IllegalStateException::new);
		assertThatIllegalArgumentException().isThrownBy(() -> consumer.accept("hello", IllegalArgumentException::new))
			.withCauseInstanceOf(IOException.class);
	}

	@Test
	void usedAsMethodParameterToMakeCallingEasier() {
		methodThatAcceptsThrowingSupplier(() -> new FileInputStream("test"));
		// Can't use below due to compile time error
		// methodThatAcceptsRegularSupplier(() -> new FileInputStream("test"));
	}

	@Test
	void usedToCallMethodThatTakesRegularFunctionalInterface() {
		methodThatAcceptsRegularSupplier(ThrowingSupplier.of(() -> new FileInputStream("test")));
	}

	private void throwIOException(String message) throws IOException {
		throw new IOException(message);
	}

	/**
	 * Example of a method that we control that accepts a {@link ThrowingSupplier}.
	 * @param supplier the supplier
	 */
	static void methodThatAcceptsThrowingSupplier(ThrowingSupplier<? extends InputStream> supplier) {
	}

	/**
	 * Example of a method that we don't control that accepts a regular {@link Supplier}.
	 * @param supplier the supplier
	 */
	static void methodThatAcceptsRegularSupplier(Supplier<? extends InputStream> supplier) {
	}

}
