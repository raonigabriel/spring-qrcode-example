package com.github.raonigabriel.qrcode;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.StreamUtils;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;

@SpringBootTest
@AutoConfigureWebTestClient
public class SpringExampleAppTests {

	@Autowired
	private ImageService imageService;	

	@Autowired
	private WebTestClient webClient;

	@Test
	public void testImageServiceQrCodeGenerationSuccess () throws Exception {
		byte[] imageBlob = imageService.generateQRCode("This is a test", 256, 256).block();
		Assertions.assertNotNull(imageBlob);

		BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageBlob));
		LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
		BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));		
		Result result = new MultiFormatReader().decode(bitmap);
		Assertions.assertNotNull(result);
		Assertions.assertEquals("This is a test", result.getText());
	}

	@Test
	public void testImageServiceQrCodeGenerationErrorNullText () throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			imageService.generateQRCode(null, 256, 256);
		});
	}

	@Test
	public void testImageServiceQrCodeGenerationErrorEmptyText () throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			imageService.generateQRCode("", 256, 256);
		});
	}

	@Test
	public void testImageServiceQrCodeGenerationErrorInvalidWidth () throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			imageService.generateQRCode("This is a test", 0, 256);
		});
	}

	@Test
	public void testImageServiceQrCodeGenerationErrorInvalidHeight () throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			imageService.generateQRCode("This is a test", 256, 0);
		});
	}

	@Test
	public void testQrCodeControllerSuccess() throws Exception {
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
}
