package com.github.spring.example;

import static org.junit.Assert.assertNotNull;

import java.util.concurrent.TimeUnit;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;

@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureWebTestClient
public class SpringExampleAppTests {

	@Autowired
	private ImageService imageService;	

	@Autowired
    private WebTestClient webClient;
	
	@Test
	public void testImageServiceQrCodeGenerationSuccess () throws Exception {
		byte[] imageBlob = imageService.generateQRCode("This is a test", 256, 256).block();
		assertNotNull(imageBlob);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testImageServiceQrCodeGenerationErrorNullText () throws Exception {
		imageService.generateQRCode(null, 256, 256);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testImageServiceQrCodeGenerationErrorEmptyText () throws Exception {
		imageService.generateQRCode("", 256, 256);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testImageServiceQrCodeGenerationErrorInvalidWidth () throws Exception {
		imageService.generateQRCode("This is a test", 0, 256);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testImageServiceQrCodeGenerationErrorInvalidHeight () throws Exception {
		imageService.generateQRCode("This is a test", 256, 0);
	}
	
	@Test
	public void testQrCodeControllerSuccess() throws Exception {
		byte[] testImage = StreamUtils.copyToByteArray(getClass().getResourceAsStream("/test.png"));
		webClient.get().uri(SpringExampleApp.QRCODE_ENDPOINT + "?text=This is a test")
			.exchange().expectStatus().isOk()
			.expectHeader().contentType(MediaType.IMAGE_PNG)
			.expectHeader().cacheControl(CacheControl.maxAge(1800, TimeUnit.SECONDS))
			.expectBody(byte[].class).isEqualTo(testImage);
	}
}
