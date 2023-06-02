package com.example.demo_hh_stat;

import com.example.demo_hh_stat.entity.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

}
