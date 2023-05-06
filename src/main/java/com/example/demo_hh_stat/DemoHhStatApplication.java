package com.example.demo_hh_stat;

import com.example.demo_hh_stat.ejemplo.Filter;
import com.example.demo_hh_stat.ejemplo.GeneralLogic;
import com.example.demo_hh_stat.ejemplo.HParser;
import com.example.demo_hh_stat.ejemplo.Vacancy;

import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;

@SpringBootApplication
public class DemoHhStatApplication {

    public static void main(String[] args) {
        print();
        //GeneralLogic logic = new GeneralLogic(new Filter("Java", 1, 10));
        GeneralLogic logic = new GeneralLogic();
        logic.printNumberOfVacancies();

    }

    //просто для проверки работы класса HParser
    public static void print() {
        HParser hParser = new HParser();
        List<Vacancy> list = hParser.getVacancies(new Filter("Java", 1, 10));
        for (Vacancy vacancy : list) {
            System.out.println(vacancy.toString());
        }
    }
}
