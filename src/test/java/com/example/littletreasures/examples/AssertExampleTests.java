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

import java.time.Duration;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

/**
 * Examples for {@link Assert}.
 */
class AssertExampleTests {

	@Test
	void illegalArgumentExceptionThrownForMostMethods() {
		String thing = null;
		assertThatIllegalArgumentException().isThrownBy(() -> Assert.notNull(thing, "'thing' must not be null"));
		assertThatIllegalArgumentException().isThrownBy(() -> Assert.hasLength(thing, "'thing' must not be empty"));
		assertThatIllegalArgumentException().isThrownBy(() -> Assert.hasText(thing, "'thing' must not be empty"));
		assertThatIllegalArgumentException()
			.isThrownBy(() -> Assert.doesNotContain("bad smell", "smell", "'thing' must not contain smell"));
		assertThatIllegalArgumentException().isThrownBy(() -> Assert.isTrue(false, "It must be true"));
	}

	@Test
	void messagesCanUseSupplier() {
		Assert.notNull("thing", () -> getSlowToCreateMessage());
	}

	@Test
	void assertStateThrowsIllegalStateException() {
		String thing = "bye bye";
		assertThatIllegalStateException().isThrownBy(() -> Assert
			.state(StringUtils.startsWithIgnoreCase(thing, "hello"), "'thing' must start with \"hello\""));
	}

	@Test
	void whyNotToUseObjectsRequireNonNull() {
		String thing = null;
		assertThatNullPointerException().isThrownBy(() -> Objects.requireNonNull(thing, "'thing' must not be null"));
	}

	private String getSlowToCreateMessage() {
		try {
			Thread.sleep(Duration.ofMinutes(5).toMillis());
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
		return "Hello";
	}

	void example(Boolean arg) {
		Objects.requireNonNull(arg, "arg must not be null");
		Assert.notNull(arg, "Arg must not be null");
		// See Assert for more
		Assert.state(StringUtils.hasLength(""), "Must not be");
	}

}
