package com.example.demo_hh_stat;

import com.example.demo_hh_stat.entity.Country;
import com.example.demo_hh_stat.entity.Filter;
import com.example.demo_hh_stat.entity.ListVacancies;
import com.example.demo_hh_stat.entity.Vacancy;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class HhApi {
    private ObjectMapper objectMapper;
    private WebClient webClient;
    final int size = 16 * 1024 * 1024;

    public HhApi(){
        objectMapper = new ObjectMapper();
        webClient = WebClient.builder().baseUrl("https://api.hh.ru")
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size))
                        .build()).build();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public List<Vacancy> getVacanciesFilterNameRegion(String nameVacancy, String nameRegion){
        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri("/vacancies?" + "text=" + nameVacancy + "&area=" + getIdRegion(nameRegion) + "&responses_count_enabled=true")
                .retrieve();
        return getVacancies(responseSpec);
    }

    private List<Vacancy> getVacancies(WebClient.ResponseSpec responseSpec) {
        String body = responseSpec.bodyToMono(String.class).block();
        System.out.println(body);
        try {
            ListVacancies lV = objectMapper.readValue(body,ListVacancies.class);
            return lV.getItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vacancy> getVacanciesFilterNameRegionExperience(String nameVacancy, String nameRegion, String idExperience){
        WebClient.ResponseSpec responseSpecNre = webClient.get()
                .uri("/vacancies?" + "text=" + nameVacancy + "&area=" + getIdRegion(nameRegion) + "&experience=" + idExperience  + "&responses_count_enabled=true")
                .retrieve();
        return getVacancies(responseSpecNre);
    }

    public List<Vacancy> getVacanciesFilterNameRegionSalary(String nameVacancy, String nameRegion, String salary){
        WebClient.ResponseSpec responseSpecNrs = webClient.get()
                .uri("/vacancies?" + "text=" + nameVacancy + "&area=" + getIdRegion(nameRegion) + "&salary=" + salary  + "&responses_count_enabled=true")
                .retrieve();
        return getVacancies(responseSpecNrs);
    }

    public List<Vacancy> getVacanciesFilterNameRegionExperienceSalary(String nameVacancy, String nameRegion, String idExperience, String salary){
        WebClient.ResponseSpec responseSpecNres = webClient.get()
                .uri("/vacancies?" + "text=" + nameVacancy + "&area=" + getIdRegion(nameRegion) + "&experience=" + idExperience + "&salary=" + salary   + "&responses_count_enabled=true")
                .retrieve();
        return getVacancies(responseSpecNres);
    }
    public String getIdRegion(String nameRegion){
        String idAreas = null;
        WebClient.ResponseSpec responseSpecArea = webClient.get()
                .uri("/areas/113")
                .retrieve();
        String bodyArea = responseSpecArea.bodyToMono(String.class).block();
        System.out.println(bodyArea);
        try {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return idAreas;
    }
}
