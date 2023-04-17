package org.mokka.stat_bot.interfaces;

import org.mokka.stat_bot.models.Response;

/* содержит метод для парсинга JSON объекта в объект типа Response */
public interface ResponseParser {
    Response parseToResponse();
}
