package com.example.demo_hh_stat.segundo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Logic {
    private HhApi hhApi;

    @Autowired
    public Logic(HhApi hhApi) {
        this.hhApi = hhApi;
    }
    public int getNumberOfVacancies(String title, String region) {
        //Filter filter = new FilterBuilder().setTitle(title).setNameRegion(region).build();
        List<Vacancy> vacancies = hhApi.getVacanciesFilterNameRegion(title, region);
        return vacancies.size();
    }
}
