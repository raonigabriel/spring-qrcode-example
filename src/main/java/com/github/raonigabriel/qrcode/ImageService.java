/*
 * Copyright 2022 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.raonigabriel.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.slf4j.*;
import org.springframework.cache.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import io.nayuki.fastqrcodegen.QrCode;
import io.nayuki.fastqrcodegen.QrCodeGenerator;

@Service
public class ImageService {

	private static final Logger LOGGER = LoggerFactory.getLogger(ImageService.class);

	public static final String CACHE_NAME = "qr-code-cache";
	
	@Cacheable(cacheNames = ImageService.CACHE_NAME, sync = true)
	public byte[] generateQRCode(String text, int width, int height)  {

		Assert.hasText(text, "text must not be empty");
		Assert.isTrue(width > 0, "width must be greater than zero");
		Assert.isTrue(height > 0, "height must be greater than zero");
		
		LOGGER.info("Will generate image  text=[{}], width=[{}], height=[{}]", text, width, height);

		try {
			final QrCode qrCode = QrCode.encodeText(text, QrCode.Ecc.MEDIUM);
			final BufferedImage img = QrCodeGenerator.toImage(qrCode, 8, 5);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(img, MediaType.IMAGE_PNG.getSubtype(), baos);
			return baos.toByteArray();
		} catch(IOException ex) {
			throw new RuntimeException(ex);
		}
	}

	@CacheEvict(cacheNames = CACHE_NAME, allEntries = true)
	public void purgeCache() {
		LOGGER.info("Purging cache");
	}

}