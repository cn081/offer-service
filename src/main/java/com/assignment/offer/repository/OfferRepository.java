package com.assignment.offer.repository;

import com.assignment.offer.resources.Offer;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OfferRepository {

	Offer save(Offer offerRequest);

	Optional<Offer> getById(String id);

	List<Offer> getAll();

	void delete(String id);
}
