package unibo.wisFacade;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

//Spring effettua i test con RandomPort così da evitare conflitti in caso di test multipli
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FacadeApplicationTests {

	//con autowired fa sì che il parametro venga inizializzato nella fase iniziale
	@Autowired
	private TestRestTemplate template;

	//Verifico che alla richiesta "/" ritorni la pagina html della Facade
    @Test
    public void getPage() throws Exception {
        ResponseEntity<String> response = template.getForEntity("/", String.class);
        assertThat(response.getBody()).contains("ServiceStatusGUI");
    }
}