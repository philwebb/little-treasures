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

package com.example.littletreasures.service;

import java.util.ArrayList;
import java.util.List;

import com.example.littletreasures.data.Hotel;
import com.example.littletreasures.data.HotelProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatIllegalStateException;

/**
 * Tests for {@link HotelsService}.
 */
class HotelsServiceTests {

	private HotelProperties data;

	private HotelsService service;

	@BeforeEach
	void setup() {
		this.data = new HotelProperties(1, createSampleHotels());
		this.service = new HotelsService(this.data);
	}

	@Test
	void createWhenFileVersionIsNotSupportedThrowsException() {
		HotelProperties hotels = new HotelProperties(2, createSampleHotels());
		assertThatIllegalStateException().isThrownBy(() -> new HotelsService(hotels))
			.withMessage("Only version 1 is supported");
	}

	@Test
	void getAllGetsAllHotels() {
		assertThat(this.service.getAll()).hasSize(5);
	}

	@Test
	void findByNameWhenNameIsNullThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> this.service.findByName(null))
			.withMessage("'name' must not be empty");
	}

	@Test
	void findByNameWhenNameIsEmptyStringThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> this.service.findByName(""))
			.withMessage("'name' must not be empty");
	}

	@Test
	void findByNameWhenFoundReturnsHotel() {
		assertThat(this.service.findByName("n3")).isEqualTo(this.data.hotels().get(2));
	}

	@Test
	void findByNameWhenFoundWithDifferentCaseReturnsHotel() {
		assertThat(this.service.findByName("N3")).isEqualTo(this.data.hotels().get(2));
	}

	@Test
	void findByNameWhenNotFoundReturnsNull() {
		assertThat(this.service.findByName("missing")).isNull();
	}

	@Test
	void findByGeographicOrderWhenGeographicOrderIsEmptyThrowsException() {
		assertThatIllegalArgumentException().isThrownBy(() -> this.service.findByGeographicOrder(""))
			.withMessage("'geographicOrder' must not be empty");
	}

	@Test
	void findByGeographicOrderWhenFoundReturnsListOfHotels() {
		assertThat(this.service.findByGeographicOrder("east")).containsExactly(this.data.hotels().get(3),
				this.data.hotels().get(4));
	}

	@Test
	void findByGeographicOrderWhenFoundWithDifferentCaseReturnsListOfHotels() {
		assertThat(this.service.findByGeographicOrder("EAsT")).containsExactly(this.data.hotels().get(3),
				this.data.hotels().get(4));
	}

	@Test
	void findByGeographicOrderWhenNotFoundReturnsEmptyList() {
		assertThat(this.service.findByGeographicOrder("north")).isEmpty();
	}

	private List<Hotel> createSampleHotels() {
		List<Hotel> hotels = new ArrayList<>();
		hotels.add(new Hotel("n1", "a1", null, 1, "1991", "o1", "t1", null, "south"));
		hotels.add(new Hotel("n2", "a2", null, 2, "1992", "o2", "t2", null, "south"));
		hotels.add(new Hotel("n3", "a3", null, 3, "1993", "o3", "t3", null, "south"));
		hotels.add(new Hotel("n4", "a4", null, 4, "1994", "o4", "t4", null, "east"));
		hotels.add(new Hotel("n5", "a5", null, 5, "1995", "o5", "t5", null, "east"));
		return hotels;
	}

}
