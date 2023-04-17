package org.mokka.stat_bot.interfaces;

import org.mokka.stat_bot.models.Vacancy;

/* содержит метод для парсинга JSON объекта в объект типа Vacancy */
public interface VacancyParser {
    Vacancy parseToVacancy();
}
