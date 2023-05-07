package com.example.tgbotspringboot;

import com.example.tgbotspringboot.Entity.Filter;
import com.example.tgbotspringboot.Entity.Vacancy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
@Component
public class GeneralLogic {
    private HhApi hhApi;
    private List<Vacancy> list;

    @Autowired
    public GeneralLogic(HhApi hhApi) {
        this.hhApi = hhApi;
        list = new ArrayList<>();
    }

    public List<Vacancy> getVacancies(Filter filter) {
        list = hhApi.getVacanciesFilterNameRegion(filter);
        return list;
    }

    public List<Vacancy> getList() {
        return list;
    }

    public int getNumberOfVacancies() {
        return list.size();
    }

    public int getNumberOfAllResponses() {
        int numberOfAllResponses = 0;
        for (Vacancy v : list) {
            numberOfAllResponses += v.getResponses();
        }
        return numberOfAllResponses;
    }

    public int getAvgResponsesNumberPerVacancy() {
        return getNumberOfAllResponses() / getNumberOfVacancies();
    }

    public void printNumberOfVacancies() {
        System.out.println("Всего вакансий: " + getNumberOfVacancies());
    }

    public void printNumberOfAllResponses() {
        System.out.println("Общее количество откликов: " + getNumberOfAllResponses());
    }

    public void printAvgResponsesNumberPerVacancy() {
        System.out.println("Среднее количество откликов на 1 вакансию: " + getAvgResponsesNumberPerVacancy());
    }

    public void printAllInformation() {
        printNumberOfVacancies();
        printNumberOfAllResponses();
        printAvgResponsesNumberPerVacancy();
    }
}
