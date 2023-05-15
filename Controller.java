package com.Celestia.PayForBlobs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class Controller {

    @GetMapping("/seed/{seed}")
    public ResponseEntity<MyResponse> seed(@PathVariable Long seed) {

        SecureRandom random = new SecureRandom();
        random.setSeed(seed);

        String nID = generateRandHexEncodedNamespaceID(random);

        String msg = generateRandMessage(random);

        //String value1 = "My hex-encoded namespace ID: " + nID;
        //String value2 = "My hex-encoded message: " + msg;

        String value1 = nID;
        String value2 = msg;

        MyResponse response = new MyResponse(value1, value2);

        return ResponseEntity.ok(response);
    }

    private static String generateRandHexEncodedNamespaceID(SecureRandom random) {
        byte[] nID = new byte[8];
        random.nextBytes(nID);
        return bytesToHex(nID);
    }

    private static String generateRandMessage(SecureRandom random) {
        int lenMsg = random.nextInt(100);
        byte[] msg = new byte[lenMsg];
        random.nextBytes(msg);
        return bytesToHex(msg);
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    @GetMapping("/response/{namespace_id}/{data}/{gas_limit}/{fee}")
    public String response(@PathVariable String namespace_id,
                                               @PathVariable String data,
                                               @PathVariable int gas_limit,
                                               @PathVariable int fee) throws JsonProcessingException {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("namespace_id", namespace_id);
        requestBody.put("data", data);
        requestBody.put("gas_limit", gas_limit);
        requestBody.put("fee", fee);
        String requestBodyJson = new ObjectMapper().writeValueAsString(requestBody);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBodyJson, headers);

        ResponseEntity<String> responseEntity = restTemplate.exchange(
                "http://localhost:26659/submit_pfb",
                HttpMethod.POST,
                requestEntity,
                String.class);

        String responseBody = responseEntity.getBody();

        return responseBody;
    }

    @GetMapping("/result/{namespace_id}/{height}")
    public String result(@PathVariable String namespace_id,
                           @PathVariable int height) throws JsonProcessingException {

        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject("http://localhost:26659/namespaced_shares/"+namespace_id+"/height/"+height, String.class);

        return response;
    }


}


