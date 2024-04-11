package com.fullstack.contactapi;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApplicationTests {

	@Autowired
	TestRestTemplate restTemplate;

	private static final String BASE_URL = "/contacts";

	@Test
	void shouldReturnAllContactsWhenListIsRequested() {
		ResponseEntity<String> response = restTemplate.getForEntity(BASE_URL, String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldReturnAPageOfContacts() {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(BASE_URL)
				.queryParam("page", 0)
				.queryParam("size", 10);
		ResponseEntity<String> response = restTemplate
				.getForEntity(uriBuilder.toUriString(), String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		/*DocumentContext documentContext = JsonPath.parse(response.getBody());
		JSONArray page = documentContext.read("$[*]");
		assertThat(page.size()).isEqualTo(1);*/
	}

	@Test
	void shouldNotReturnContactThatDoNotExist() {
		ResponseEntity<String> response = restTemplate
				.getForEntity(BASE_URL + "/101", String.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldCreateANewContact() {
		Contact newContact = new Contact();
		newContact.setName("Rob Zombie");
		newContact.setEmail("zombie@records.net");
		newContact.setTitle("Dragula");
		newContact.setPhone("984-5789-6321");
		newContact.setAddress("Los Angeles, CA");
		newContact.setStatus("Active");
		ResponseEntity<Void> response = restTemplate.postForEntity(BASE_URL, newContact, Void.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI locationOfNewCashCard = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewCashCard, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@Disabled
	void shouldRequestProfilePictureFormExistingContact() throws Exception{
		final String id = "37bfd772-5208-4627-9ddf-d0d9f01485f5";
		ResponseEntity<Contact> response = restTemplate
				.getForEntity(BASE_URL + "/" + id, Contact.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		String photoUrl = response.getBody().getPhotoUrl();
		assertNotNull(photoUrl);

		ResponseEntity<byte[]> getResponse =
		    restTemplate.getForEntity(BASE_URL + "/image/" + id + ".png", byte[].class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
		assertThat(getResponse.getBody()).isNotEmpty();
	}

	@Test
	@DirtiesContext
	void shouldDeleteAContact() {
		//ResponseEntity<Contact[]> response = restTemplate.getForEntity(BASE_URL, Contact[].class);

		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(BASE_URL)
				.queryParam("page", 0)
				.queryParam("size", 5);

		ResponseEntity<ContactResponse> response =
				restTemplate.getForEntity(uriBuilder.toUriString(), ContactResponse.class);
		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		ContactResponse contactResponse = response.getBody();

		assertThat(contactResponse).isNotNull();
		List<Contact> contactList = contactResponse.getContactList();
		assertThat(contactList).isNotEmpty();

		int totalPages = contactResponse.getTotalPages();
		assertThat(totalPages).isGreaterThan(1);

		Contact deleteContact = contactList.get(contactList.size() - 1); // Last Item

		ResponseEntity<Void> delResponse = restTemplate
				.exchange(BASE_URL + "/" + deleteContact.getId(), HttpMethod.DELETE, null, Void.class);

		assertThat(delResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> getResponse = restTemplate
				.getForEntity(BASE_URL + "/" + deleteContact.getId(), String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
