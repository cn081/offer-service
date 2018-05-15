package com.assignment.offer;

import com.assignment.offer.repository.OfferRepository;
import com.assignment.offer.resources.Offer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Random;
import java.util.UUID;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static springfox.documentation.builders.PathSelectors.regex;

@Slf4j
@SpringBootApplication
@EnableScheduling
public class OfferApplication {

	private final OfferRepository offerRepository;

	@Autowired
	public OfferApplication(OfferRepository offerRepository) {
		this.offerRepository = offerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(OfferApplication.class, args);
	}

	@PostConstruct
	public void loadSampleOffers() {
		final Random random = new Random();
		IntStream.range(1, 10)
				.forEach(i -> {
					final Offer offer = new Offer();
					offer.setId(UUID.randomUUID().toString());
					offer.setIsoCurrencyCode("GBP");
					offer.setPrice(new BigDecimal(random.nextInt(25) + 10));
					offer.setExpiresAt(LocalDateTime.now().plusMinutes(random.nextInt(60) + 30));
					offer.setDescription(format("Offer # %s", random.nextInt(999) + 100));

					log.info(format("Loading %s into cache", offer));

					offerRepository.save(offer);
				});

		log.info(format("\n\n#### %s offers loaded into cache ####\n\n", offerRepository.getAll().size()));
	}

	@Configuration
	@EnableSwagger2
	public class SwaggerConfiguration {

		@Bean
		public Docket docket() {

			return new Docket(DocumentationType.SWAGGER_2)
					.apiInfo(createApiInfo())
					.select()
					.apis(RequestHandlerSelectors.basePackage("com.assignment.offer"))
					.paths(regex("/offers.*"))
					.build();
		}

		private ApiInfo createApiInfo() {
			return
					new ApiInfo(
							"API documention for offer-service",
							"A simple RESTful software service that allows merchants to create and manage a simple offer",
							"1.0",
							null,
							new Contact("developer", "url", "develop@email"),
							null,
							null,
							Collections.emptyList());
		}
	}
}
