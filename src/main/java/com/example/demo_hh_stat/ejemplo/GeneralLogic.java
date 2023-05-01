package com.example.demo_hh_stat.ejemplo;

import java.util.List;

public class GeneralLogic {
    Filter filter;
    HParser hParser = new HParser();
    List<Vacancy> list = hParser.getVacancies(filter);

    public GeneralLogic(Filter filter) {
        this.filter = filter;
    }

    public int getNumberOfVacancies() {
        int numberOfVacancies = list.size();
        return numberOfVacancies;
    }

    public int getNumberOfAllResponses() {
        int numberOfAllResponses = 0;
        for (Vacancy v : list) {
            numberOfAllResponses += v.getResponses();
        }
        return numberOfAllResponses;
    }

    public int getAvgResponsesNumberPerVacancy() {
        int avgResponsesPerVacancy = getNumberOfAllResponses() / getNumberOfVacancies();
        return avgResponsesPerVacancy;
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
