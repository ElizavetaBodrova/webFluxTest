package ru.neoflex.controller;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import com.github.tomakehurst.wiremock.WireMockServer;

import javax.ws.rs.client.Client;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@DirtiesContext
public class TestControllerTest {

    private WireMockServer wireMockServer;

    private Client client;

    @Before
    public void setup() {
        wireMockServer = new WireMockServer(wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        //client = new Client("http://localhost:" + wireMockServer.port());
    }

    @After
    public void tearDown() {
        wireMockServer.stop();
    }
@Ignore
    @Test
    public void test3() {
        int singleRequestTime = 5;


        /*stubFor(get(urlEqualTo("/test/" + 3)).willReturn(aResponse().withFixedDelay(singleRequestTime)
                .withStatus(200)
                .withHeader("Content-Type", "application/json")
                .withBody(String.format("{ \"id\": %d }", i))));*/


        long start = System.currentTimeMillis();
       // String stringMono = client.test3().toString().block();
        long end = System.currentTimeMillis();

        long totalExecutionTime = end - start;

       // assertEquals("Result of test3: ", stringMono, "test3");
        assertEquals("time is 5 sec: ", singleRequestTime, totalExecutionTime);
    }


}