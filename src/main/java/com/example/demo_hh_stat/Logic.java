package com.example.demo_hh_stat;

import com.example.demo_hh_stat.entity.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.lang.reflect.Field;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Logic {
    private final HhApi hhApi;
    public List<Vacancy> getVacancyFilter(Filter filter){
        Field field[] = filter.getClass().getDeclaredFields();
        field[2].setAccessible(true);
        field[3].setAccessible(true);
        try {
            if (field[2].get(filter) == null && field[3].get(filter) == null) {
                return hhApi.getVacanciesFilterNameRegion(filter.getTitle(),filter.getRegion());
            }
            if (field[2].get(filter) != null && field[3].get(filter) == null) {
                return hhApi.getVacanciesFilterNameRegionExperience(filter.getTitle(),filter.getRegion(),overRideExperience(filter.getExperience()));
            }
            if (field[2].get(filter) == null && field[3].get(filter) != null){
                return hhApi.getVacanciesFilterNameRegionSalary(filter.getTitle(),filter.getRegion(),String.valueOf(filter.getSalary()));
            }
            if (field[2].get(filter) != null && field[3].get(filter) != null){
                return hhApi.getVacanciesFilterNameRegionExperienceSalary(filter.getTitle(), filter.getRegion(), overRideExperience(filter.getExperience()), String.valueOf(filter.getSalary()));
            }
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public int getNumberOfVacancies(List<Vacancy> list) {
        list = getVacancyFilter(Filter.builder().build());
        return list.size();
    }

    public int getAllResponses(List<Vacancy> list) {
        int totalResponses = 0;
        for (Vacancy v : list) {
            totalResponses += v.getCounters().getResponses();
        }
        return totalResponses;
    }

    public String overRideExperience(String nameExperience){
        String massive[] = {"noExperience","between1And3","between3And6","moreThan6"};
        String massiveNameExperience[] = nameExperience.split(" ");
        char s[] = massiveNameExperience[0].toCharArray();
        for (int i = 0; i < s.length; i++){
            if (s[i] == ','){
                s[i] = '.';
            }
        }
        massiveNameExperience[0] = String.valueOf(s);
        if (Double.parseDouble(massiveNameExperience[0]) < 1){
            return massive[0];
        }
        if (Double.parseDouble(massiveNameExperience[0]) >= 1 && Double.parseDouble(massiveNameExperience[0]) < 3){
            return massive[1];
        }
        if (Double.parseDouble(massiveNameExperience[0]) >= 3 && Double.parseDouble(massiveNameExperience[0]) <= 6){
            return massive[2];
        }
        if (Double.parseDouble(massiveNameExperience[0]) > 6){
            return massive[3];
        }
        return null;
    }
}

