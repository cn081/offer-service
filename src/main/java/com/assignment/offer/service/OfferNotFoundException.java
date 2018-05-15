package com.assignment.offer.service;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
class OfferNotFoundException extends RuntimeException {
	OfferNotFoundException(String message) {
		super(message);
	}
}
