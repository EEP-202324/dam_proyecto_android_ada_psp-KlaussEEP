package com.epp.interino.interns;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class InternsJsonTest {
	
	@Autowired
    private JacksonTester<Interns> json;
	
	@Test
	void internsSerializationTest() throws IOException {
		Interns interns = new Interns(1, "Pepe", "Perez", 1000.00);
        assertThat(json.write(interns)).isStrictlyEqualToJson("expected.json");

        assertThat(json.write(interns)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(interns)).extractingJsonPathNumberValue("@.id")
        	.isEqualTo(1);
        
        assertThat(json.write(interns)).hasJsonPathStringValue("@.name");
        assertThat(json.write(interns)).extractingJsonPathStringValue("@.name")
        	.isEqualTo("Pepe");
        
        assertThat(json.write(interns)).hasJsonPathStringValue("@.surname");
        assertThat(json.write(interns)).extractingJsonPathStringValue("@.surname")
        	.isEqualTo("Perez");
        
        assertThat(json.write(interns)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(interns)).extractingJsonPathNumberValue("@.amount")
             .isEqualTo(1000.00);
    }
	
	@Test
	void internsDeserializationTest() throws IOException {
	   String expected = """
	           {
	               "id":1000,
	               "name":"Javier",
	               "surname":"Lopez",
	               "amount":67.89
	           }
	           """;
	   assertThat(json.parse(expected))
	           .isEqualTo(new Interns(1000,"Javier", "Lopez", 67.89));
	   assertThat(json.parseObject(expected).id()).isEqualTo(1000);
	   assertThat(json.parseObject(expected).name()).isEqualTo("Javier");
	   assertThat(json.parseObject(expected).surname()).isEqualTo("Lopez");
	   assertThat(json.parseObject(expected).amount()).isEqualTo(67.89);
	}
}

