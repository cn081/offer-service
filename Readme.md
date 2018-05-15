# Offer service

A simple spring boot RESTful software service that allows merchants to create and manage a simple offer.

Sample offers are loaded at startup.  curl to the /offers endpoint e.g.

```
curl http://localhost:8080/offers
```

## Swagger API
[Swagger API](http://localhost:8080/swagger-ui.html)

In addition to the running the integration test, you can interact with the API using swagger, url above and sample request payloads below:

- Create Offer

url: [create offer](http://localhost:8080/swagger-ui.html#!/offer45controller/createUsingPOST)

example request:
```
{"id":null,"price":15,"isoCurrencyCode":"GBP","description":"Offer null","expiresAt":"2018-05-15T10:51:50.531","expired":false}
```

example response:
```
{
  "offer": {
    "id": "905f34d1-a8e5-415f-8460-4ca084aac204",
    "price": 41,
    "isoCurrencyCode": "GBP",
    "description": "Offer null",
    "expiresAt": "2018-05-15T11:32:12.597",
    "expired": false
  },
  "_links": {
    "GET": [
      {
        "href": "http://localhost:56871/offers"
      },
      {
        "href": "http://localhost:56871/offers/905f34d1-a8e5-415f-8460-4ca084aac204"
      }
    ],
    "DELETE": {
      "href": "http://localhost:56871/offers/905f34d1-a8e5-415f-8460-4ca084aac204"
    }
  }
}
```


## Improvements
- Return different types of exceptions for expiry and not found
- OfferExpirationService can possibly use a data structure that allows you to set an eviction policy on write
- Provide a custom BigDecimal serializer to the ObjectMapper to ensure the offer price is formated correctly

## Running
compile:
```
./gradlew clean build
```

run:
```
java -jar build/libs/offer-service-0.0.1-SNAPSHOT.jar
```


You can run with a custom port by running:
```
 java -Dserver.port=5050 -jar build/libs/offer-service-0.0.1-SNAPSHOT.jar
```
