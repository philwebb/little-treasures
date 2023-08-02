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

import java.nio.charset.StandardCharsets;

import com.example.littletreasures.data.Hotel;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.UpperCamelCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriUtils;

/**
 * JSON response containing a summary of a single {@link Hotel}.
 */
@JsonNaming(UpperCamelCaseStrategy.class)
@JsonInclude(Include.NON_NULL)
public class HotelSummary {

	private String name;

	private String address;

	private String image;

	private String architects;

	private String geographicOrder;

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getImage() {
		return this.image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getArchitects() {
		return this.architects;
	}

	public void setArchitects(String architects) {
		this.architects = architects;
	}

	public String getGeographicOrder() {
		return this.geographicOrder;
	}

	public void setGeographicOrder(String geographicOrder) {
		this.geographicOrder = geographicOrder;
	}

	static HotelSummary get(Hotel hotel) {
		if (hotel == null) {
			return null;
		}
		PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
		HotelSummary result = new HotelSummary();
		map.from(hotel::name).as(StringUtils::capitalize).to(result::setName);
		map.from(hotel::address).to(result::setAddress);
		map.from(hotel::image)
			.as((image) -> UriUtils.encodePath(image, StandardCharsets.UTF_8))
			.as((image) -> "http://localhost:8080/images/" + image)
			.to(result::setImage);
		map.from(hotel::architects)
			.as((collection) -> StringUtils.collectionToDelimitedString(collection, " | "))
			.to(result::setArchitects);
		map.from(hotel::geographicOrder).to(result::setGeographicOrder);
		return result;
	}

}
