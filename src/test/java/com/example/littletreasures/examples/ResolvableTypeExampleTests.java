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

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import org.springframework.core.ResolvableType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Examples for {@link ResolvableType}.
 */
class ResolvableTypeExampleTests {

	MultiValueMap<String, Integer> map = new LinkedMultiValueMap<>();

	List<? extends CharSequence> list;

	@Test
	void instanceCannotResolveGenerics() {
		Object instance = ReflectionTestUtils.getField(this, "map");
		System.out.println(instance);
		// We can test if instance if a map but we can't test from the instance alone the
		// generics because of type erasure
		assertThat(instance).isInstanceOf(Map.class);
	}

	@Test
	void fieldCanResolveGenerics() throws NoSuchFieldException, SecurityException {
		// ... but the information is in the bytecode so if we track things we can resolve
		// generics
		ResolvableType type = ResolvableType.forField(getClass().getDeclaredField("map"));
		assertThat(type).hasToString("org.springframework.util.MultiValueMap<java.lang.String, java.lang.Integer>");
		assertThat(type.resolveGeneric(0)).isEqualTo(String.class);
		assertThat(type.resolveGeneric(1)).isEqualTo(Integer.class);
	}

	@Test
	void resolvableTypeTracksGenerics() throws NoSuchFieldException, SecurityException {
		ResolvableType type = ResolvableType.forField(getClass().getDeclaredField("map"));
		ResolvableType asMap = type.asMap();
		ResolvableType valueType = asMap.getGeneric(1);
		assertThat(valueType).hasToString("java.util.List<java.lang.Integer>");
		assertThat(valueType.resolveGeneric(0)).isEqualTo(Integer.class);
		assertThat(asMap.resolveGeneric(1, 0)).isEqualTo(Integer.class);
		assertThat(asMap.resolveGeneric(0)).isEqualTo(String.class);
	}

	@Test
	void resolvableTypeCanCheckAssignment() throws NoSuchFieldException, SecurityException {
		ResolvableType type = ResolvableType.forField(getClass().getDeclaredField("list"));
		assertThat(type.isAssignableFrom(ResolvableType.forClassWithGenerics(List.class, String.class))).isTrue();
		assertThat(type.isAssignableFrom(ResolvableType.forClassWithGenerics(List.class, Integer.class))).isFalse();
	}

}
