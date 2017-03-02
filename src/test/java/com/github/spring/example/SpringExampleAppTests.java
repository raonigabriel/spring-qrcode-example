package com.github.spring.example;

import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringExampleAppTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;
	
	@Autowired
	private ImageService imageService;	

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}
	
	@Test
	public void testImageServiceQrCodeGenerationSuccess () throws Exception {
		byte[] imageBlob = imageService.generateQRCode("This is a test", 256, 256);
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
	
	public void testQrCodeControllerSuccess() throws Exception {
		
		byte[] testImage = StreamUtils.copyToByteArray(getClass().getResourceAsStream("/test.png"));
		
		mockMvc.perform(get(SpringExampleApp.QRCODE_ENDPOINT + "?text=This is a test"))
			.andExpect(status().isOk())
			.andExpect(content().contentType(MediaType.IMAGE_PNG))
			.andExpect(header().string("Cache-Control", "max-age=1800"))
			.andExpect(content().bytes(testImage));
	}
}
