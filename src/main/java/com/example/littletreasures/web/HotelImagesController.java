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

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.core.log.LogMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.ConcurrentReferenceHashMap;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.util.function.ThrowingSupplier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * {@link Controller @Controller} to show the hotel images.
 */
@RestController
class HotelImagesController {

	private final Map<String, byte[]> cache = new ConcurrentReferenceHashMap<>();

	private static final Log logger = LogFactory.getLog(HotelImagesController.class);

	private final AtomicInteger cacheMisses = new AtomicInteger();

	private final AtomicInteger totalRequests = new AtomicInteger();

	@GetMapping(path = "/images/{name}", produces = MediaType.IMAGE_JPEG_VALUE)
	Resource image(@PathVariable String name) throws IOException {
		int totalRequests = this.totalRequests.incrementAndGet();
		byte[] thumbnail = this.cache.get(name);
		if (thumbnail != null) {
			logger.debug(LogMessage.format("Cache hit for image %s", name));
			return new ByteArrayResource(thumbnail);
		}
		logger.debug(LogMessage.format("Cache miss for image %s", name));
		logger.trace(LogMessage.of(() -> "Cache miss ratio " + getCacheMissRatio(totalRequests)));
		logger.trace(LogMessage.of(() -> getImageUrl(name)));
		byte[] raw = FileCopyUtils.copyToByteArray(getClass().getResourceAsStream("/images/" + name));
		if (ObjectUtils.isEmpty(raw)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		thumbnail = createThumbnail(raw);
		this.cache.put(name, thumbnail);
		return new ByteArrayResource(thumbnail);
	}

	private String getImageUrl(String name) {
		return ThrowingSupplier.of(() -> ResourceUtils.getURL("/images/" + name).toString()).get();
	}

	private float getCacheMissRatio(int totalRequests) {
		int cacheMisses = this.cacheMisses.incrementAndGet();
		return (float) cacheMisses / totalRequests;
	}

	private byte[] createThumbnail(byte[] raw) throws IOException {
		BufferedImage image = ImageIO.read(new ByteArrayInputStream(raw));
		Image resultingImage = image.getScaledInstance(200, 200, Image.SCALE_DEFAULT);
		BufferedImage thumbnail = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
		thumbnail.getGraphics().drawImage(resultingImage, 0, 0, null);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		ImageIO.write(thumbnail, "jpg", out);
		simulateSlowOperation();
		return out.toByteArray();
	}

	private void simulateSlowOperation() {
		try {
			Thread.sleep(Duration.ofSeconds(1).toMillis());
		}
		catch (InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

}
