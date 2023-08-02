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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;

import org.springframework.boot.util.LambdaSafe;

import static org.assertj.core.api.Assertions.assertThatRuntimeException;

/**
 * Examples for {@link LambdaSafe}.
 */
@SuppressWarnings("unchecked")
public class LambdaSafeExampleTests {

	@Test
	@SuppressWarnings("rawtypes")
	void invokeDirectly() {
		List<Consumer<?>> consumers = getConsumers();
		Integer value = 123;
		for (Consumer consumer : consumers) {
			try {
				consumer.accept(value);
			}
			catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	@Test
	@SuppressWarnings("rawtypes")
	void invokeDirectlyIgnoreFailure() {
		List<Consumer<?>> consumers = getConsumers();
		Integer value = -123;
		for (Consumer consumer : consumers) {
			try {
				consumer.accept(value);
			}
			catch (Exception ex) {
			}
		}
	}

	@Test
	void invokeSafely() {
		// See LambdaSafe.isLambdaGenericProblem
		List<Consumer<?>> consumers = getConsumers();
		Integer value = 123;
		LambdaSafe.callbacks(Consumer.class, consumers, value).invoke((consumer) -> consumer.accept(value));
	}

	@Test
	void invokeSafelyWithFailure() {
		List<Consumer<?>> consumers = getConsumers();
		Integer value = -123;
		assertThatRuntimeException().isThrownBy(() -> LambdaSafe.callbacks(Consumer.class, consumers, value)
			.invoke((consumer) -> consumer.accept(value)));
	}

	private List<Consumer<?>> getConsumers() {
		List<Consumer<?>> consumers = new ArrayList<>();
		consumers.add((Consumer<Integer>) this::onInteger);
		consumers.add((Consumer<String>) this::onString);
		return consumers;
	}

	void onInteger(Integer value) {
		if (value < 0) {
			throw new RuntimeException("badness");
		}
		System.out.println("I got integer " + value);
	}

	void onString(String value) {
		System.out.println("I got string " + value);
	}

}
