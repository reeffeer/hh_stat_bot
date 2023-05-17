package com.example.demo_hh_stat.segundo;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class HhApi {
    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    public HhApi(){
        httpClient = HttpClient.newHttpClient();
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Vacancy> getVacanciesFilterNameRegion(String nameVacancy, String nameRegion){
        HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create("https://api.hh.ru/vacancies?" + "text=" + nameVacancy + "&area=" + getIdRegion(nameRegion))).build();
        try {
            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
            String body = response.body();
            System.out.println(body);
            ListVacancies lV = objectMapper.readValue(body,ListVacancies.class);
            return lV.getItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public String getIdRegion(String nameRegion){
        HttpRequest httpRequestArea = HttpRequest.newBuilder().uri(URI.create("https://api.hh.ru/areas/113")).build();
        String idAreas = null;
        try {
            HttpResponse<String> responseArea = httpClient.send(httpRequestArea, HttpResponse.BodyHandlers.ofString());
            String bodyArea = responseArea.body();
            Country cT = objectMapper.readValue(bodyArea, Country.class);
            System.out.println(nameRegion);
            for (int i = 0; i < cT.getAreas().size(); i++){
                for (int j = 0; j < cT.getAreas().get(i).getAreas().size(); j++){
                    if (nameRegion.equals("Москва")){
                        idAreas = "1";
                        break;
                    } else {
                        if (cT.getAreas().get(i).getAreas().get(j).getName().equals(nameRegion)){
                            idAreas = cT.getAreas().get(i).getAreas().get(j).getId();
                            break;
                        }
                    }
                }
                if (idAreas != null){
                    break;
                }
            }
            System.out.println(idAreas);
            if (idAreas == null){
                idAreas = "2";
            }
            return idAreas;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
