package com.example.demo_hh_stat.segundo;

import com.example.demo_hh_stat.ejemplo.Filter;
import com.example.demo_hh_stat.ejemplo.FilterBuilder;
import com.example.demo_hh_stat.ejemplo.Vacancy;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Parameter;
import java.util.List;

@Component
@NoArgsConstructor
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
