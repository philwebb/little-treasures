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

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.example.littletreasures.data.Hotel;
import com.example.littletreasures.data.HotelProperties;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedCaseInsensitiveMap;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 * Hotel service used to access the hotel data.
 */
@Service
public class HotelsService {

	private final List<Hotel> all;

	private final Map<String, Hotel> byName;

	private MultiValueMap<String, Hotel> byGeographicOrder;

	HotelsService(HotelProperties properties) {
		Assert.state(properties.fileVersion() == 1, "Only version 1 is supported");
		LinkedCaseInsensitiveMap<Hotel> byName = new LinkedCaseInsensitiveMap<>();
		List<Hotel> hotels = properties.hotels();
		hotels.forEach((hotel) -> byName.put(hotel.name(), hotel));
		MultiValueMap<String, Hotel> byGeographicOrder = new LinkedMultiValueMap<>();
		hotels.forEach((hotel) -> byGeographicOrder.add(hotel.geographicOrder().toLowerCase(), hotel));
		this.all = Collections.unmodifiableList(hotels);
		this.byName = Collections.unmodifiableMap(byName);
		this.byGeographicOrder = CollectionUtils.unmodifiableMultiValueMap(byGeographicOrder);
	}

	public List<Hotel> getAll() {
		return this.all;
	}

	public Hotel findByName(String name) {
		Assert.hasLength(name, "'name' must not be empty");
		return this.byName.get(name);
	}

	public List<Hotel> findByGeographicOrder(String geographicOrder) {
		Assert.hasLength(geographicOrder, "'geographicOrder' must not be empty");
		return this.byGeographicOrder.getOrDefault(geographicOrder.toLowerCase(), Collections.emptyList());
	}

}
