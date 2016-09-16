package org.synyx.urlaubsverwaltung.core.mail;

import org.junit.Assert;
import org.junit.Test;

import org.synyx.urlaubsverwaltung.core.person.Person;
import org.synyx.urlaubsverwaltung.test.TestDataCreator;

import java.util.List;


/**
 * @author  Aljona Murygina - murygina@synyx.de
 */
public class RecipientUtilTest {

    @Test
    public void ensureFiltersOutPersonsWithoutMailAddress() {

        Person person = TestDataCreator.createPerson("muster", "Max", "Mustermann", "max@muster.de");
        Person anotherPerson = TestDataCreator.createPerson("mmuster", "Marlene", "Muster", "marlene@muster.de");
        Person personWithoutMailAddress = TestDataCreator.createPerson("nomail", "No", "Mail", null);

        List<String> recipients = RecipientUtil.getMailAddresses(person, anotherPerson, personWithoutMailAddress);

        Assert.assertEquals("Wrong number of recipients", 2, recipients.size());
        Assert.assertTrue("Missing recipient", recipients.contains("max@muster.de"));
        Assert.assertTrue("Missing recipient", recipients.contains("marlene@muster.de"));
    }
}
