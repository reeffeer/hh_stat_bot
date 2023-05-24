package com.example.demo_hh_stat;

import com.example.demo_hh_stat.entity.Filter;
import com.example.demo_hh_stat.entity.Vacancy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Logic {
    private HhApi hhApi;

    public Logic(HhApi hhApi) {
        this.hhApi = hhApi;
    }
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
