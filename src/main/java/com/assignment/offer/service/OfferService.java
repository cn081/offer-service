package com.assignment.offer.service;

import com.assignment.offer.repository.OfferRepository;
import com.assignment.offer.resources.Offer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@Slf4j
public class OfferService {

	private OfferRepository offerRepository;

	@Autowired
	public OfferService(OfferRepository offerRepository) {
		this.offerRepository = offerRepository;
	}

	public Offer createOffer(Offer offerRequest) {
		if (Objects.isNull(offerRequest.getId())) {
			offerRequest.setId(UUID.randomUUID().toString());
		}

		return offerRepository.save(offerRequest);
	}

	public Offer getOffer(String id) {
		return
				offerRepository.getById(id)
						.filter(isNotExpired())
						.orElseThrow(() ->
								new OfferNotFoundException(format("No offer found, mathcing id: %s", id)));
	}

	public List<Offer> getAllOffers() {
		return offerRepository.getAll().stream().filter(isNotExpired()).collect(Collectors.toList());
	}

	public Offer cancelOffer(String id) {
		final Offer offer = getOffer(id);
		offer.setExpired(Boolean.TRUE);
		offer.setExpiresAt(LocalDateTime.now());

		return offerRepository.save(offer);
	}

	private Predicate<Offer> isNotExpired() {
		return offer -> !offer.getExpired();
	}
}
