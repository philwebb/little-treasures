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

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.example.littletreasures.data.Hotel;
import com.example.littletreasures.service.HotelsService;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.FileCopyUtils;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for {@link HotelsController}.
 */
@WebMvcTest(HotelsController.class)
class HotelsControllerTests {

	@MockBean
	private HotelsService service;

	@Autowired
	private MockMvc mvc;

	@Test
	void slashHotelsReturnsAll() throws Exception {
		assertAllHotelsReturned("/hotels");
	}

	@Test
	void slashHotelsSlashReturnsAll() throws Exception {
		assertAllHotelsReturned("/hotels/");
	}

	private void assertAllHotelsReturned(String url) throws Exception {
		List<Hotel> hotels = new ArrayList<>();
		hotels.add(new Hotel("n1", "a1", null, 1, "1991", "o1", "t1", null, "south"));
		hotels.add(new Hotel("n2", "a2", null, 2, "1992", "o2", "t2", null, "east"));
		given(this.service.getAll()).willReturn(hotels);
		this.mvc.perform(get(url))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(from("hotels-all.json")));
	}

	@Test
	void slashHotelsSlashNameWhenFoundReturnsHotel() throws Exception {
		Hotel hotel = new Hotel("n1", "a1", null, 1, "1991", "o1", "t1", null, "south");
		given(this.service.findByName("n1")).willReturn(hotel);
		this.mvc.perform(get("/hotels/n1"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(from("hotel.json")));
	}

	@Test
	void slashSearchSlashGeographicorderSlashNameWhenFoundReturnsHotels() throws Exception {
		List<Hotel> hotels = new ArrayList<>();
		hotels.add(new Hotel("n1", "a1", null, 1, "1991", "o1", "t1", null, "south"));
		hotels.add(new Hotel("n2", "a2", null, 2, "1992", "o2", "t2", null, "south"));
		given(this.service.findByGeographicOrder("south")).willReturn(hotels);
		this.mvc.perform(get("/hotels/search/geographicorder/south"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json(from("hotels-geographic-order.json")));
	}

	@Test
	void slashSearchSlashGeographicorderSlashNameWhenNotFoundEmptyJson() throws Exception {
		given(this.service.findByGeographicOrder("south")).willReturn(Collections.emptyList());
		this.mvc.perform(get("/hotels/search/geographicorder/south"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.APPLICATION_JSON))
			.andExpect(content().json("[]"));
	}

	@Test
	void slashHotelsSlashNameWhenNotFoundReturns404() throws Exception {
		this.mvc.perform(get("/hotels/n1")).andExpect(status().isNotFound());
	}

	private String from(String path) throws IOException {
		ClassPathResource resource = new ClassPathResource(path, getClass());
		InputStreamReader reader = new InputStreamReader(resource.getInputStream());
		return FileCopyUtils.copyToString(reader);
	}

}
