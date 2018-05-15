package com.assignment.offer.resources;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@NoArgsConstructor
@Data
@ToString
public class Offer {

	private String id;
	private BigDecimal price;
	private String isoCurrencyCode;
	private String description;
	private LocalDateTime expiresAt;
	private Boolean expired = Boolean.FALSE;

	public Offer fromOffer() {
		final Offer offer = new Offer();
		offer.setId(this.id);
		offer.setPrice(this.price);
		offer.setIsoCurrencyCode(this.isoCurrencyCode);
		offer.setDescription(this.description);
		offer.setExpiresAt(this.expiresAt);
		offer.setExpired(this.expired);

		return offer;
	}
}
