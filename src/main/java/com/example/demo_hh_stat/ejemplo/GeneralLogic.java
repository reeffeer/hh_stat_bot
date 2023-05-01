package com.example.demo_hh_stat.ejemplo;

import java.util.ArrayList;
import java.util.List;

public class GeneralLogic {
    HParser hParser = new HParser();
    Filter filter = new Filter("Java", 1, 10);
    List list = new ArrayList<>(hParser.getVacancies(filter.getTitle(), filter.getCityId(), filter.getPageNumber()));

    public int getNumberOfVacancies() {
//        int numberOfVacancies = hParser.getVacancies("Java", 1/*, 11*/
//                /*filter.getTitle(), filter.getCityId(), filter.getPageNumber()*/)
//                .size(); //вместо конкретных данных мы получаем значения фильтров из класса Filter,
        int numberOfVacancies = list.size();
        return numberOfVacancies;
    }

    public int getNumberOfAllResponses() {
        int numberOfAllResponses = 0; //TODO
        hParser.getVacancies(filter.getTitle(), filter.getCityId(), filter.pageNumber);
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
