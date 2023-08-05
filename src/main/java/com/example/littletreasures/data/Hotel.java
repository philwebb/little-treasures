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

package com.example.littletreasures.data;

import java.util.List;

/**
 * A single Hotel record.
 *
 * @param name the name of the hotel
 * @param address the address of the hotel
 * @param image the hotel image, or {@code null}
 * @param rooms the number of rooms
 * @param opened the year that the hotel was opened
 * @param operator the operator of the hotel
 * @param theme the hotel theme, or {@code null}
 * @param architects the architects of the hotel (if known)
 * @param geographicOrder the geographic order of the hotel (for example, "south strip")
 * @see HotelProperties
 */
public record Hotel(String name, String address, String image, int rooms, String opened, String operator, String theme,
		List<String> architects, String geographicOrder) {

}
