package com.assignment.offer.repository;

import com.assignment.offer.resources.Offer;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryOfferRepository implements OfferRepository {

	private List<Offer> offers;

	public InMemoryOfferRepository() {
		this.offers = new ArrayList<>();
	}

	public Offer save(Offer offerRequest) {

		final Offer savedOffer = offerRequest.fromOffer();
		offers.add(savedOffer);
		return savedOffer;
	}

	public Optional<Offer> getById(String id) {
		return offers.stream().filter(offer -> offer.getId().equals(id)).findFirst();
	}

	public List<Offer> getAll() {
		return offers;
	}

	public void delete(String id) {
		getById(id).ifPresent(offer -> {
			offer.setExpired(Boolean.TRUE);
			offer.setExpiresAt(LocalDateTime.now());
		});
	}
}
