package com.assignment.offer.resources;

import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;

@Getter
public class OfferResource extends ResourceSupport {

	private final Offer offer;

	public OfferResource(Offer offer) {
		this.offer = offer;
	}
}
