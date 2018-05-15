package com.assignment.offer.resources;

import com.assignment.offer.repository.OfferRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OfferControllerIntegrationTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockBean
	private OfferRepository offerRepository;

	@Test
	public void shouldGetOffer() {

		final String offerId = UUID.randomUUID().toString();
		final Offer offer = createOffer(offerId);

		given(offerRepository.getById(offerId)).willReturn(Optional.of(offer));

		this.webTestClient
				.get()
				.uri(format("/offers/%s", offerId))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(entityExchangeResult -> log.info(entityExchangeResult.toString()))
				.jsonPath("$.offer.id").isEqualTo(offerId)
		;
	}

	@Test
	public void shouldNotGetOfferIfExpired() {

		final String offerId = UUID.randomUUID().toString();
		final Offer offer = createOffer(offerId);
		offer.setExpired(true);

		given(offerRepository.getById(offerId)).willReturn(Optional.of(offer));

		this.webTestClient
				.get()
				.uri(format("/offers/%s", offerId))
				.exchange()
				.expectStatus()
				.isNotFound()
				.expectBody()
				.consumeWith(entityExchangeResult -> log.info(entityExchangeResult.toString()))
		;
	}

	@Test
	public void shouldGetAllOffers() {

		final String offerId1 = UUID.randomUUID().toString();
		final String offerId2 = UUID.randomUUID().toString();
		final List<Offer> offers = createOffers(Arrays.asList(offerId1, offerId2));

		given(offerRepository.getAll()).willReturn(offers);

		this.webTestClient
				.get()
				.uri("/offers")
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody()
				.consumeWith(entityExchangeResult -> log.info(entityExchangeResult.toString()))
				.jsonPath("$.length()").isEqualTo(2)
		;
	}

	@Test
	public void shouldCreateOffer() {
		final Offer offerRequest = createOffer(null);

		final Offer savedOffer = offerRequest.fromOffer();
		savedOffer.setId(UUID.randomUUID().toString());

		given(offerRepository.save(any(Offer.class))).willReturn(savedOffer);

		this.webTestClient
				.post()
				.uri("/offers")
				.body(BodyInserters.fromObject(offerRequest))
				.exchange()
				.expectStatus()
				.isCreated()
				.expectBody(String.class)
				.consumeWith(entityExchangeResult -> log.info(entityExchangeResult.toString()))
		;
	}

	@Test
	public void shouldCancelOffer() {
		final String offerId = UUID.randomUUID().toString();
		final Offer offer = createOffer(offerId);

		given(offerRepository.getById(offerId)).willReturn(Optional.of(offer));
		given(offerRepository.save(any(Offer.class))).willReturn(offer);

		this.webTestClient
				.delete()
				.uri(format("/offers/%s", offerId))
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(String.class)
				.consumeWith(entityExchangeResult -> log.info(entityExchangeResult.toString()))
		;
	}

	private List<Offer> createOffers(List<String> offerIds) {
		return offerIds.stream().map(this::createOffer).collect(Collectors.toList());
	}

	private Offer createOffer(String offerId) {
		final Random random = new Random();
		final Offer offer = new Offer();
		offer.setId(offerId);
		offer.setPrice(new BigDecimal(random.nextInt(50) + 10));
		offer.setIsoCurrencyCode("GBP");
		offer.setDescription(format("Offer %s", offerId));
		offer.setExpiresAt(LocalDateTime.now().plusMinutes(random.nextInt(60) + 5));

		return offer;
	}
}
