package com.example.demo_hh_stat.segundo;

import lombok.Getter;

@Getter
public class Filter {
    private final String title;
    private final String region;
    private final String experience;
    private final int salary;
    private final String requestId;

    public Filter(FilterBuilder filterBuilder) {

        this.title = filterBuilder.title;
        this.region = filterBuilder.region;
        this.experience = filterBuilder.experience;
        this.salary = filterBuilder.salary;
        this.requestId = filterBuilder.requestId;
    }

    @Override
    public String toString() {
        return "Filter{" +
                "nameVacancy='" + title + '\'' +
                ", nameRegion='" + region + '\'' +
                ", experience='" + experience + '\'' +
                ", salary=" + salary +
                ", requestId='" + requestId + '\'' +
                '}';
    }

    public static class FilterBuilder {
        private final String title;
        private final String region;
        private final String requestId;
        private String experience;
        private int salary;

        public FilterBuilder(String title, String region, String requestId) {
            this.title = title;
            this.region = region;
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
            return new Filter(this);
        }
    }
}
