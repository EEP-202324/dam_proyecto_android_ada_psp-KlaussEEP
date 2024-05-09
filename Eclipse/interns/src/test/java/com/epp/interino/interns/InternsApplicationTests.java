package com.epp.interino.interns;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import net.minidev.json.JSONArray;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@DirtiesContext(classMode = ClassMode.AFTER_EACH_TEST_METHOD)
class InternsApplicationTests {
	@Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAnInternWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("Javier", "j").getForEntity("/interns/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        Number id = documentContext.read("$.id");
        assertThat(id).isEqualTo(1);
        
        String nombre = documentContext.read("$.name");
        assertThat(nombre).isEqualTo("Pepe");
        
        String apellido = documentContext.read("$.surname");
        assertThat(apellido).isEqualTo("Perez");
        
        Double amount = documentContext.read("$.amount");
        assertThat(amount).isEqualTo(1000);
        
        String boss = documentContext.read("$.boss");
        assertThat(boss).isEqualTo("Javier");
    }

    @Test
    void shouldNotReturnAnInternWithAnUnknownId() {
      ResponseEntity<String> response = restTemplate.withBasicAuth("Javier", "j").getForEntity("/interns/1000", String.class);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody()).isBlank();
    }
    
    
    @Test
    @DirtiesContext
    void shouldCreateANewIntern() {
    	   Interns newInterns = new Interns(1, "Pepe", "Perez", 1000, "Javier");
    	   ResponseEntity<Void> createResponse = restTemplate.withBasicAuth("Javier", "j").postForEntity("/interns", newInterns, Void.class);
    	   assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

    	   URI locationOfNewIntern = createResponse.getHeaders().getLocation();
    	   ResponseEntity<String> getResponse = restTemplate.withBasicAuth("Javier", "j").getForEntity(locationOfNewIntern, String.class);
    	   assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    	   
    	   DocumentContext documentContext = JsonPath.parse(getResponse.getBody());
           Number id = documentContext.read("$.id");
           assertThat(id).isEqualTo(1);
           
           String nombre = documentContext.read("$.name");
           assertThat(nombre).isEqualTo("Pepe");
           
           String apellido = documentContext.read("$.surname");
           assertThat(apellido).isEqualTo("Perez");
           
           Double amount = documentContext.read("$.amount");
           assertThat(amount).isEqualTo(1000);
           
           String boss = documentContext.read("$.boss");
           assertThat(boss).isEqualTo("Javier");
    }
    
    @Test
    void shouldReturnAllInternsWhenListIsRequested() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("Javier", "j").getForEntity("/interns", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        
        DocumentContext documentContext = JsonPath.parse(response.getBody());
        int internsCount = documentContext.read("$.length()");
        assertThat(internsCount).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactlyInAnyOrder(1, 2, 3);
        
        JSONArray names = documentContext.read("$..name");
        assertThat(names).containsExactlyInAnyOrder("Pepe", "Juan", "Sancho");
        
        JSONArray surnames = documentContext.read("$..surname");
        assertThat(surnames).containsExactlyInAnyOrder("Perez", "Lama", "Ramos");

        JSONArray amounts = documentContext.read("$..amount");
        assertThat(amounts).containsExactlyInAnyOrder(1000.00, 1200.00, 1100.00);
        
        JSONArray bosses = documentContext.read("$..boss");
        assertThat(bosses).containsExactlyInAnyOrder("Javier", "Javier", "Javier");
    }
    
    @Test
    void shouldReturnAPageOfInterns() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("Javier", "j").getForEntity("/interns?page=0&size=1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(1);
    }
    
    @Test
    void shouldReturnASortedPageOfInterns() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("Javier", "j").getForEntity("/interns?page=0&size=1&sort=id,asc", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray read = documentContext.read("$[*]");
        assertThat(read.size()).isEqualTo(1);

        int id = documentContext.read("$[0].id");
        assertThat(id).isEqualTo(1);
    }
    
    @Test
    void shouldReturnASortedPageOfInternsWithNoParametersAndUseDefaultValues() {
        ResponseEntity<String> response = restTemplate.withBasicAuth("Javier", "j").getForEntity("/interns", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        DocumentContext documentContext = JsonPath.parse(response.getBody());
        JSONArray page = documentContext.read("$[*]");
        assertThat(page.size()).isEqualTo(3);

        JSONArray ids = documentContext.read("$..id");
        assertThat(ids).containsExactly(1, 2, 3);
    }
    
    @Test
    void shouldNotReturnACashCardWhenUsingBadCredentials() {
        ResponseEntity<String> response = restTemplate
          .withBasicAuth("BAD-USER", "j")
          .getForEntity("/interns/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);

        response = restTemplate
          .withBasicAuth("Javier", "BAD-PASSWORD")
          .getForEntity("/interns/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
    
    @Test
    void shouldRejectUsersWhoAreNotCardOwners() {
        ResponseEntity<String> response = restTemplate
          .withBasicAuth("hank-owns-no-cards", "qrs456")
          .getForEntity("/interns/1", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }
    
    @Test
    void shouldNotAllowAccessToCashCardsTheyDoNotOwn() {
        ResponseEntity<String> response = restTemplate
          .withBasicAuth("javier", "j")
          .getForEntity("/interns/5", String.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
