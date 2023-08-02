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

package com.example.littletreasures.web;

import java.util.List;

import com.example.littletreasures.service.HotelsService;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * {@link RestController @RestController} providing {@link HotelSummary} JSON.
 */
@RestController
@RequestMapping(path = "/hotels", produces = MediaType.APPLICATION_JSON_VALUE)
class HotelsController {

	private final HotelsService hotelsService;

	HotelsController(HotelsService hotelsService) {
		this.hotelsService = hotelsService;
	}

	@GetMapping({ "", "/" })
	List<HotelSummary> all() {
		return this.hotelsService.getAll().stream().map(HotelSummary::get).toList();
	}

	@GetMapping("/{name}")
	HotelSummary byName(@PathVariable String name) {
		HotelSummary hotel = HotelSummary.get(this.hotelsService.findByName(name));
		if (hotel == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		return hotel;
	}

	@GetMapping("/search/geographicorder/{name}")
	List<HotelSummary> byGeographicOrder(@PathVariable String name) {
		return this.hotelsService.findByGeographicOrder(name).stream().map(HotelSummary::get).toList();
	}

}
