package com.example.demo_hh_stat;

import com.example.demo_hh_stat.entity.Filter;
import com.example.demo_hh_stat.entity.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Logic {
    private final HhApi hhApi;

    public int getNumberOfVacancies(Filter filter) {
        List<Vacancy> vacancies = hhApi.getVacanciesFilterNameRegion(filter.getTitle(), filter.getRegion());
        return vacancies.size();
    }
    public int getAllResponses(Filter filter) {
        List<Vacancy> vacancies = hhApi.getVacanciesFilterNameRegion(filter.getTitle(), filter.getRegion());
        int totalResponses = 0;
        for (Vacancy v : vacancies) {
            totalResponses += v.getCounters().getResponses();
        }
        return totalResponses;
    }

    public List<Vacancy> getVacancyFilter(Filter filter){
        Field field[] = filter.getClass().getDeclaredFields();
        field[2].setAccessible(true);
        field[3].setAccessible(true);
        try {
            if (field[2].get(filter) == null && Integer.parseInt(String.valueOf(field[3].get(filter))) == 0){
                return hhApi.getVacanciesFilterNameRegion(filter.getTitle(),filter.getRegion());
            }
            if (field[2].get(filter) != null && Integer.parseInt(String.valueOf(field[3].get(filter))) == 0){
                return hhApi.getVacanciesFilterNameRegionExperience(filter.getTitle(),filter.getRegion(),filter.getExperience());
            }
            if (field[2].get(filter) == null && Integer.parseInt(String.valueOf(field[3].get(filter))) != 0){
                return hhApi.getVacanciesFilterNameRegionSalary(filter.getTitle(),filter.getRegion(),String.valueOf(filter.getSalary()));
            }
            if (field[2].get(filter) != null && Integer.parseInt(String.valueOf(field[3].get(filter))) != 0){
                return hhApi.getVacanciesFilterNameRegionExperienceSalary(filter.getTitle(), filter.getRegion(), filter.getExperience(), String.valueOf(filter.getSalary()));
            }
            return null;
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
