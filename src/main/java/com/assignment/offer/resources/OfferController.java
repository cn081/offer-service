package com.assignment.offer.resources;

import com.assignment.offer.service.OfferService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/offers")
public class OfferController {

	private final OfferService offerService;
	private final OfferResourceAssembler offerResourceAssembler;

	@Autowired
	public OfferController(OfferService offerService, OfferResourceAssembler offerResourceAssembler) {
		this.offerService = offerService;
		this.offerResourceAssembler = offerResourceAssembler;
	}

	@GetMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity get(@PathVariable final String id) {
		final OfferResource offerResource = offerResourceAssembler.toResource(offerService.getOffer(id));

		addGetAllOffersLink(offerResource);
		addDeleteOfferLink(offerResource);

		return ResponseEntity.ok(offerResource);
	}

	@GetMapping(produces = APPLICATION_JSON_VALUE)
	public ResponseEntity getAll() {
		final List<OfferResource> offerResources =
				offerResourceAssembler.toResources(offerService.getAllOffers());

		offerResources
				.forEach(offerResource -> {
					addGetOfferLink(offerResource);
					addDeleteOfferLink(offerResource);
				});

		return ResponseEntity.ok(offerResources);
	}

	@PostMapping(consumes = APPLICATION_JSON_VALUE)
	public ResponseEntity create(@RequestBody final Offer offerRequest) {
		final OfferResource offerResource = offerResourceAssembler.toResource(offerService.createOffer(offerRequest));

		addGetAllOffersLink(offerResource);
		addGetOfferLink(offerResource);
		addDeleteOfferLink(offerResource);

		return new ResponseEntity<>(offerResource, HttpStatus.CREATED);
	}

	@DeleteMapping(path = "/{id}", produces = APPLICATION_JSON_VALUE)
	public ResponseEntity cancel(@PathVariable final String id) {
		final OfferResource offerResource = offerResourceAssembler.toResource(offerService.cancelOffer(id));

		addGetAllOffersLink(offerResource);

		return ResponseEntity.ok(offerResource);
	}

	private void addGetAllOffersLink(OfferResource offerResource) {
		offerResource.add(linkTo(methodOn(OfferController.class).getAll()).withRel("GET"));
	}

	private void addGetOfferLink(OfferResource offerResource) {
		offerResource.add(linkTo(methodOn(OfferController.class).get(offerResource.getOffer().getId())).withRel("GET"));
	}

	private void addDeleteOfferLink(OfferResource offerResource) {
		offerResource.add(linkTo(methodOn(OfferController.class).cancel(offerResource.getOffer().getId())).withRel("DELETE"));
	}
}
