package com.example.demo_hh_stat;

import com.example.demo_hh_stat.entity.Vacancy;
import com.example.demo_hh_stat.telegramBot.UserSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Logic {
    private final HhApi hhApi;

    public List<Vacancy> getVacancyFilter(UserSession filter){
        if(filter.getExperience() == null && filter.getSalary() == null)
            return hhApi.getVacanciesFilterNameRegion(filter.getTitle(),filter.getRegion());
        if(filter.getExperience() != null && filter.getSalary() == null)
            return hhApi.getVacanciesFilterNameRegionExperience(filter.getTitle(),filter.getRegion(),overRideExperience(filter.getExperience()));
        if(filter.getExperience() == null && filter.getSalary() != null)
            return hhApi.getVacanciesFilterNameRegionSalary(filter.getTitle(),filter.getRegion(),String.valueOf(filter.getSalary()));
        if(filter.getExperience() != null && filter.getSalary() != null)
            return hhApi.getVacanciesFilterNameRegionExperienceSalary(filter.getTitle(), filter.getRegion(), overRideExperience(filter.getExperience()), String.valueOf(filter.getSalary()));
        return new ArrayList<>();
    }

//    public int getNumberOfVacancies(List<Vacancy> list) {
//        list = getVacancyFilter(Filter.builder().build());
//        return list.size();
//    }

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

