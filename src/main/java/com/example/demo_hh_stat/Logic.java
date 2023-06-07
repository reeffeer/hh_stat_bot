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
                return hhApi.getVacanciesFilterNameRegionExperience(filter.getTitle(),filter.getRegion(),filter.getExperience());
            }
            if (field[2].get(filter) == null && field[3].get(filter) != null){
                return hhApi.getVacanciesFilterNameRegionSalary(filter.getTitle(),filter.getRegion(),String.valueOf(filter.getSalary()));
            }
            if (field[2].get(filter) != null && field[3].get(filter) != null){
                return hhApi.getVacanciesFilterNameRegionExperienceSalary(filter.getTitle(), filter.getRegion(), filter.getExperience(), String.valueOf(filter.getSalary()));
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
}

