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
        //Filter filter = new FilterBuilder().setTitle(title).setNameRegion(region).build();
        List<Vacancy> vacancies = hhApi.getVacanciesFilterNameRegion(filter);
        return vacancies.size();
    }
}
