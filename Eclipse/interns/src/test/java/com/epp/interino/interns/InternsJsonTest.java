package com.epp.interino.interns;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.assertj.core.util.Arrays;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
public class InternsJsonTest {
	
	@Autowired
    private JacksonTester<Interns> json;
	
	@Autowired
    private JacksonTester<Interns[]> jsonList;
	
	private Interns[] interns;

    @BeforeEach
    void setUp() {
    	interns = Arrays.array(
                new Interns(1, "Pepe", "Perez", 1000.00),
                new Interns(2, "Juan", "Lama", 1200.00),
                new Interns(3, "Sancho", "Ramos", 1100.00));
    }
	
	@Test
	void internsSerializationTest() throws IOException {
		Interns intern = new Interns(1, "Pepe", "Perez", 1000.00);
		
		InputStream isTmpA = InternsJsonTest.class.getClassLoader().getResourceAsStream("com/epp/interino/interns/expected.json");
		
        String a = IOUtils.toString(isTmpA, StandardCharsets.UTF_8);
        
        assertThat(json.write(intern)).isStrictlyEqualToJson(a);

        assertThat(json.write(intern)).hasJsonPathNumberValue("@.id");
        assertThat(json.write(intern)).extractingJsonPathNumberValue("@.id")
        	.isEqualTo(1);
        
        assertThat(json.write(intern)).hasJsonPathStringValue("@.name");
        assertThat(json.write(intern)).extractingJsonPathStringValue("@.name")
        	.isEqualTo("Pepe");
        
        assertThat(json.write(intern)).hasJsonPathStringValue("@.surname");
        assertThat(json.write(intern)).extractingJsonPathStringValue("@.surname")
        	.isEqualTo("Perez");
        
        assertThat(json.write(intern)).hasJsonPathNumberValue("@.amount");
        assertThat(json.write(intern)).extractingJsonPathNumberValue("@.amount")
             .isEqualTo(1000.00);
    }
	
	@Test
	void internsListSerializationTest() throws IOException {
		 assertThat(jsonList.write(interns)).isStrictlyEqualToJson("list.json");
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

	@Test
	void internsListDeserializationTest() throws IOException {
		String expected="""
		        [
					{ "id":1, "name":"Pepe", "surname":"Perez", "amount":1000.00 },
					{ "id":2, "name":"Juan", "surname":"Lama", "amount":1200.00 },
					{ "id":3, "name":"Sancho", "surname":"Ramos", "amount":1100.00 }
				]
		         """;
		   assertThat(jsonList.parse(expected)).isEqualTo(interns);
	}
	
}
