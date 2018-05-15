package com.assignment.offer.service;

import com.assignment.offer.resources.Offer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@Slf4j
public class OfferExpirationService {

	private final OfferService offerService;

	@Autowired
	public OfferExpirationService(OfferService offerService) {

		this.offerService = offerService;
	}

	@Scheduled(initialDelayString = "${initialDelay.in.milliseconds}", fixedDelayString = "${fixedDelay.in.milliseconds}")
	public void expireOffers() {

		final LocalDateTime now = LocalDateTime.now();

		offerService.getAllOffers()
				.forEach(offer -> {
					if (isExpired(now, offer)) {
						log.info(String.format("Expiring offer: %s", offer.getId()));
						offer.setExpired(true);
						offerService.createOffer(offer);
					}
				});
	}

	private boolean isExpired(LocalDateTime now, Offer offer) {
		return now.isAfter(offer.getExpiresAt());
	}
}
