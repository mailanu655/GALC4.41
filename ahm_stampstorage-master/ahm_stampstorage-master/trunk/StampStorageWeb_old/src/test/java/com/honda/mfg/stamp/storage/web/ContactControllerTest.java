package com.honda.mfg.stamp.storage.web;

import com.honda.mfg.stamp.conveyor.domain.Contact;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

/**
 * Created by IntelliJ IDEA.
 * User: Ambica Gawarla
 * Date: 3/19/12
 * Time: 10:01 AM
 * To change this template use File | Settings | File Templates.
 */
@Configurable
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/META-INF/spring/applicationContext.xml")
@Transactional
public class ContactControllerTest {

    @Test
    public void successfullyTestContactController(){
        loadContactData();
        ContactController controller = new ContactController();
        assertNotNull(controller.populateContacts());
    }


    public void loadContactData(){
        Contact contact = new Contact();
        contact.setContactName("a");
        contact.setEmail("a@honda.com");
        contact.setPagerNo("a6186@honda.com");

        contact.persist();
    }
}
