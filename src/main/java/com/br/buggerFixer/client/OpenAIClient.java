package com.br.buggerFixer.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Component
public class OpenAIClient {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getSuggestedFix(String code, String expectedBehavior) {
        String input = String.format("""
            Corrija o seguinte método Java:
            
            %s
            
            De forma que ele atenda ao seguinte comportamento esperado:
            
            %s
            
            Retorne apenas o código corrigido, sem explicações, sem tambem ```java no inicio e ``` no final, apenas o metodo!.
            """, code, expectedBehavior);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "chatgpt-4o-latest");
        body.put("input", input);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                Map.class
        );

        Object result = response.getBody().get("output");

        List<Object> contentValue = (List<Object>) ((LinkedHashMap) ((ArrayList) result).get(0)).get("content");
        result = ((LinkedHashMap) contentValue.get(0)).get("text");
        return result != null ? result.toString() : "Nenhuma resposta gerada.";
    }

    public String askForMissingImports(String classContent, String methodName) {
        String input = String.format("""
        Aqui está uma classe Java com um método chamado '%s' que foi recentemente corrigido por uma IA:

        %s

        Verifique se com essa nova versão do método, são necessários novos imports na classe. 
        Caso positivo, retorne apenas os imports faltantes (um por linha) sem tambem ```java no inicio e ``` no final, sem explicações. 
        Caso não seja necessário, responda apenas com: Nenhum.
        """, methodName, classContent);

        Map<String, Object> body = new HashMap<>();
        body.put("model", "chatgpt-4o-latest");
        body.put("input", input);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.exchange(
                apiUrl,
                HttpMethod.POST,
                request,
                Map.class
        );

        Object result = response.getBody().get("output");

        List<Object> contentValue = (List<Object>) ((LinkedHashMap) ((ArrayList) result).get(0)).get("content");
        result = ((LinkedHashMap) contentValue.get(0)).get("text");

        return result != null ? result.toString().trim() : "Nenhum";
    }


}
