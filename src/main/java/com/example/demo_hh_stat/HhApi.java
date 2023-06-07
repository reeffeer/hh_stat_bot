package com.example.demo_hh_stat;

import com.example.demo_hh_stat.entity.Country;
import com.example.demo_hh_stat.entity.ListVacancies;
import com.example.demo_hh_stat.entity.Vacancy;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class HhApi {
    private final ObjectMapper objectMapper;
    private final WebClient webClient;

    public List<Vacancy> getVacanciesFilterNameRegion(String nameVacancy, String nameRegion){
        String uri = UriComponentsBuilder.fromPath("/vacancies")
                .queryParam("text", nameVacancy)
                .queryParam("area", getIdRegion(nameRegion))
                .queryParam("responses_count_enabled", true)
                .toUriString();

        WebClient.ResponseSpec responseSpec = webClient.get()
                .uri(uri)
                .retrieve();

        return getVacancies(responseSpec);
    }

    private List<Vacancy> getVacancies(WebClient.ResponseSpec responseSpec) {
        String body = responseSpec.bodyToMono(String.class).block();
        System.out.println(body);
        try {
            ListVacancies listVacancies = objectMapper.readValue(body,ListVacancies.class);
            return listVacancies.getItems();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Vacancy> getVacanciesFilterNameRegionExperience(String nameVacancy, String nameRegion, String idExperience){
        String uri = UriComponentsBuilder.fromPath("/vacancies")
                .queryParam("text", nameVacancy)
                .queryParam("area", getIdRegion(nameRegion))
                .queryParam("experience", idExperience)
                .queryParam("responses_count_enabled", true)
                .toUriString();

        WebClient.ResponseSpec responseSpecNre = webClient.get()
                .uri(uri)
                .retrieve();

        return getVacancies(responseSpecNre);
    }

    public List<Vacancy> getVacanciesFilterNameRegionSalary(String nameVacancy, String nameRegion, String salary){
        String uri = UriComponentsBuilder.fromPath("/vacancies")
                .queryParam("text", nameVacancy)
                .queryParam("area", getIdRegion(nameRegion))
                .queryParam("salary", salary)
                .queryParam("responses_count_enabled", true)
                .toUriString();

        WebClient.ResponseSpec responseSpecNrs = webClient.get()
                .uri(uri)
                .retrieve();

        return getVacancies(responseSpecNrs);
    }

    public List<Vacancy> getVacanciesFilterNameRegionExperienceSalary(String nameVacancy, String nameRegion, String idExperience, String salary){
        String uri = UriComponentsBuilder.fromPath("/vacancies")
                .queryParam("text", nameVacancy)
                .queryParam("area", getIdRegion(nameRegion))
                .queryParam("experience", idExperience)
                .queryParam("salary", salary)
                .queryParam("responses_count_enabled", true)
                .toUriString();

        WebClient.ResponseSpec responseSpecNres = webClient.get()
                .uri(uri)
                .retrieve();

        return getVacancies(responseSpecNres);
    }

    public String getIdRegion(String nameRegion){
        String idAreas;
        WebClient.ResponseSpec responseSpecArea = webClient.get()
                .uri("/areas/113")
                .retrieve();
        String bodyArea = responseSpecArea.bodyToMono(String.class).block();
        System.out.println(bodyArea);
        try {
            Country country = objectMapper.readValue(bodyArea, Country.class);
            System.out.println(nameRegion);

            Optional<String> idAreasOptional = country.getAreas().stream()
                    .flatMap(area -> area.getAreas().stream())
                    .filter(area -> nameRegion.equals("Москва") || area.getName().equals(nameRegion))
                    .map(area -> nameRegion.equals("Москва") ? "1" : area.getId())
                    .findFirst();

            idAreas = idAreasOptional.orElse("2");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return idAreas;
    }
}
