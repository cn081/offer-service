package com.assignment.offer.resources;

import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Component;

@Component
public class OfferResourceAssembler extends ResourceAssemblerSupport<Offer, OfferResource> {

	public OfferResourceAssembler() {
		super(OfferController.class, OfferResource.class);
	}

	@Override
	public OfferResource toResource(Offer offer) {
		return new OfferResource(offer);
	}
}