package com.example.tgbotspringboot.Entity;

import lombok.Getter;

@Getter
public class Filter {
    private final String nameVacancy;
    private final String nameRegion;
    private final String experience;
    private final int salary;
    private final String requestId;

    public Filter(FilterBuilder builder) {
        this.nameVacancy = builder.nameVacancy;
        this.nameRegion = builder.nameRegion;
        this.experience = builder.experience;
        this.salary = builder.salary;
        this.requestId = builder.requestId;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "nameVacancy='" + nameVacancy + '\'' +
                ", nameRegion='" + nameRegion + '\'' +
                ", experience='" + experience + '\'' +
                ", salary=" + salary +
                ", requestId='" + requestId + '\'' +
                '}';
    }

    public static class FilterBuilder {
        private final String nameVacancy;
        private final String nameRegion;
        private final String requestId;
        private String experience;
        private int salary;

        public FilterBuilder(String nameVacancy, String nameRegion, String requestId) {
            this.nameVacancy = nameVacancy;
            this.nameRegion = nameRegion;
            this.requestId = requestId;
        }

        public FilterBuilder experience(String experience) {
            this.experience = experience;
            return this;
        }

        public FilterBuilder salary(int salary) {
            this.salary = salary;
            return this;
        }

        public Filter build() {
            Filter filter = new Filter(this);
            return filter;
        }

    }

    }
