package com.github.raonigabriel.qrcode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.Cache;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

@AutoConfigureWebTestClient
@SpringBootTest(classes = {SpringExampleApp.class, ImageService.class})
class SpringExampleAppTests {

	@Autowired
	private ImageService imageService;

	@Autowired
	private WebTestClient webClient;
	
	@Autowired
    private CacheManager cacheManager;
	
	private String text;
	
	private int width;
	
	private int height;
	
	@BeforeEach
	void beforeEach() {
		text = "This is a test";
		width = 256;
		height = 256;
	}

	@Test
	void given_valid_params_should_generate_readable_qr () throws Exception {
		// Given, When
		byte[] imageBlob = imageService.generateQRCode(text, width, height);

		// Then
		assertNotNull(imageBlob);
		assertReadableQr(imageBlob);
	}

	@Test
	void given_null_text_should_throw_exception () throws Exception {
		
		// Given
		text = null;
		
		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			imageService.generateQRCode(text, width, height);
		});
	}

	@Test
	void given_empty_text_should_throw_exception () throws Exception {
		
		// Given
		text = "";

		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			imageService.generateQRCode(text, width, height);
		});
	}

	@Test
	void given_zero_width_should_throw_exception () throws Exception {

		// Given
		width = 0;
		
		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			imageService.generateQRCode(text, width, height);
		});
	}

	@Test
	void given_zero_height_should_throw_exception () throws Exception {
		
		// Given
		height = 0;
		
		// Then
		assertThrows(IllegalArgumentException.class, () -> {
			// When
			imageService.generateQRCode(text, width, height);
		});
	}

	@Test
	void testQrCodeControllerSuccess() throws Exception {
		byte[] testImage;
		// When running using Azul "Zulu JDK", the generated image is smaller. We need to take this into
		// consideration, because Github Actions is using Zulu JDK.
		if (System.getProperty("java.vendor").contains("Azul Systems")) {
			testImage = StreamUtils.copyToByteArray(getClass().getResourceAsStream("/test-azul.png"));
		} else {
			testImage = StreamUtils.copyToByteArray(getClass().getResourceAsStream("/test.png"));
		}
		webClient.get().uri(SpringExampleApp.QRCODE_ENDPOINT + "?text=This is a test")
		.exchange().expectStatus().isOk()
		.expectHeader().contentType(MediaType.IMAGE_PNG)
		.expectHeader().cacheControl(CacheControl.maxAge(1800, TimeUnit.SECONDS))
		.expectHeader().contentLength(testImage.length)
		.expectBody(byte[].class).isEqualTo(testImage);
	}
	
	@Test
	void given_populated_cache_when_delete_then_cache_gets_cleared() throws Exception {
		
		// Given
		generateQr();
		assertQrIsCached();
		
		// When
		webClient.delete().uri(SpringExampleApp.QRCODE_ENDPOINT).exchange().expectStatus().isNoContent();
		
		// Then
		assertQrIsNotCached();
	}
	
	@Test
	void given_empty_cache_when_generate_qr_should_add_to_cache() throws Exception {

		// Given
		clearCache();
		
		// When
		generateQr();

		// Then
		assertQrIsCached();
    }
	
	private void clearCache() {
		final Cache imageCache = cacheManager.getCache(ImageService.CACHE_NAME);
		imageCache.invalidate();		
	}
	
	private void generateQr() {
		final String text = "This is a test";
		byte[] imageBlob = imageService.generateQRCode(text, width, height);
		assertNotNull(imageBlob);		
	}
	
	private void assertReadableQr(byte[] imageBlob) throws IOException, NotFoundException {
		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBlob));
		LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));		
		Result result = new MultiFormatReader().decode(bitmap);
		assertNotNull(result);
		assertEquals(text, result.getText());
	}
	
	private void assertQrIsCached() {
		final Cache imageCache = cacheManager.getCache(ImageService.CACHE_NAME);
		final ValueWrapper valueWrapper = imageCache.get(new SimpleKey(text, 256, 256));
		assertNotNull(valueWrapper);
		assertNotNull(valueWrapper.get());
	}
	
	private void assertQrIsNotCached() {
		final Cache imageCache = cacheManager.getCache(ImageService.CACHE_NAME);
		final ValueWrapper valueWrapper = imageCache.get(new SimpleKey(text, 256, 256));
		assertNull(valueWrapper);
	}
}
