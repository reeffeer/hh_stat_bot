package com.example.demo_hh_stat.ejemplo;

import java.util.List;

public class GeneralLogic {
    Filter filter = new Filter("Java", 1, 10);
    HParser hParser = new HParser();
    List<Vacancy> list = hParser.getVacancies(filter);

    /*public GeneralLogic(Filter filter) {
        this.filter = filter;
    }*/
    public GeneralLogic() {
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
