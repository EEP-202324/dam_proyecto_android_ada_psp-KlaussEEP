package com.epp.interino.interns;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InternsApplicationTests {
	@Autowired
    TestRestTemplate restTemplate;

    @Test
    void shouldReturnAnInternWhenDataIsSaved() {
        ResponseEntity<String> response = restTemplate.getForEntity("/interns/1", String.class);

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
    }

    @Test
    void shouldNotReturnAnInternWithAnUnknownId() {
      ResponseEntity<String> response = restTemplate.getForEntity("/interns/1000", String.class);

      assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
      assertThat(response.getBody()).isBlank();
    }
}
