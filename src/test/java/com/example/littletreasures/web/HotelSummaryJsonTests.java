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

import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONCompareMode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Json tests for {@link HotelSummary}.
 */
@JsonTest
class HotelSummaryJsonTests {

	// We could also use JacksonTester.initFields(this, objectMapper);

	@Autowired
	private JacksonTester<HotelSummary> json;

	@Test
	void writeJson() throws IOException {
		HotelSummary info = new HotelSummary();
		info.setName("Doge's palace");
		info.setAddress("Production");
		info.setArchitects("Madhura Bhave | Phil Webb");
		info.setGeographicOrder("south");
		assertThat(this.json.write(info)).isEqualToJson("""
				{"Name": "Doge's palace",
				"Address": "Production",
				"Architects": "Madhura Bhave | Phil Webb",
				"GeographicOrder": "south"}""");
	}

	@Test
	void writeJsonWithNulls() throws IOException {
		HotelSummary info = new HotelSummary();
		info.setName("Doge's palace");
		info.setGeographicOrder("south");
		assertThat(this.json.write(info)).isEqualToJson("""
				{"Name": "Doge's palace",
				"GeographicOrder": "south"}""", JSONCompareMode.STRICT);
	}

	@Test
	void readJson() throws IOException {
		// Our app doesn't read JSON, this test just shows what's possible
		assertThat(this.json.read("hotel-info.json")).satisfies((hotelInfo) -> {
			assertThat(hotelInfo.getName()).isEqualTo("Doge's palace");
			assertThat(hotelInfo.getAddress()).isEqualTo("Production");
			assertThat(hotelInfo.getArchitects()).isEqualTo("Madhura Bhave | Phil Webb");
		});
	}

}
