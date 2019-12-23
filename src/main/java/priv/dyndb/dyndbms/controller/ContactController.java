package priv.dyndb.dyndbms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import priv.dyndb.dyndbms.dao.ContactDaoDBM;
import priv.dyndb.dyndbms.item.Contact;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/contacts")
public class ContactController {

    @Autowired
    private ContactDaoDBM contactDaoDBM;

    /*@RequestMapping("/")
    @ResponseBody
    public String welcome() {
        return "Dynamo DB Rest Webservices.";
    }*/

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Contact> greeting() throws IOException {
        return contactDaoDBM.list();
    }

    @RequestMapping(value="/{name}", method = RequestMethod.DELETE)
    public void delete(@PathVariable("name") String name) {
        contactDaoDBM.delete(name);
    }

    @RequestMapping(value="/{name}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Contact get(@PathVariable("name") String name) {
        return contactDaoDBM.get(name);
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Contact add(@RequestBody Contact contact) throws IOException {
        contactDaoDBM.add(contact);
        return contact;
    }

    @RequestMapping(method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE,consumes = MediaType.APPLICATION_JSON_VALUE)
    public Contact modify(@RequestBody Contact contact) throws IOException {
        contactDaoDBM.add(contact);
        return contact;
    }


}
