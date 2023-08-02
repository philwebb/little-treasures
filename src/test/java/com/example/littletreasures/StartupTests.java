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

package com.example.littletreasures;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.boot.test.util.TestPropertyValues;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link Startup}.
 */
@ExtendWith(OutputCaptureExtension.class)
class StartupTests {

	@Test
	void printAuthIfNecessaryWhenNoPropertyDoesNotPrint(CapturedOutput out) {
		Startup.printAuthIfNecessary();
		assertThat(out).doesNotContain("AUTH:");
	}

	@Test
	void printAuthIfNecessaryWhenPropertyTruePrints(CapturedOutput out) {
		TestPropertyValues.of("com.example.production:true").applyToSystemProperties(Startup::printAuthIfNecessary);
		assertThat(out).contains("AUTH: LITTLE-TREASURE");
	}

	@Test
	void printAuthIfNecessaryWhenPropertyFalsePrints(CapturedOutput out) {
		TestPropertyValues.of("com.example.production:false").applyToSystemProperties(Startup::printAuthIfNecessary);
		assertThat(out).doesNotContain("AUTH:");
	}

	@Test
	@Disabled
	void doingThingsManually() {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream capture = new PrintStream(out);
		PrintStream previous = System.out;
		try {
			System.setOut(capture);
			System.setProperty("com.example.production", "true");
			try {
				Startup.printAuthIfNecessary();
			}
			finally {
				System.clearProperty("com.example.production");
			}
		}
		finally {
			System.setOut(previous);
		}
		assertThat(out.toString(StandardCharsets.UTF_8)).contains("AUTH: LITTLE-TREASURE");
	}

}
