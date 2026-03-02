package org.justserve


import net.datafaker.Faker
import net.datafaker.providers.base.Address

class TestUser {

    public String email, firstName, lastName, password, zipcode, countryCode, country, locale
    public UUID uuid = null

    /**
     *  constructor that generates fake data for the model's properties.
     *
     *  @param faker seed for a faker, say a specific locale.
     */
    TestUser(Faker faker) {
        this.email = faker.internet().emailAddress()
        this.firstName = faker.name().firstName()
        this.lastName = faker.name().lastName()
        Address address = faker.address()
        this.zipcode = address.zipCode()
        this.countryCode = address.countryCode()
        this.country = address.country()
        this.locale = faker.locality().localeString()
        this.password = faker.credentials().password(8,100,true,true,true)
    }
}
