package com.example.demo_hh_stat.ejemplo;

import com.google.gson.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class HParser {

    URL obj = null;
    private List<Vacancy> vacancyList = new ArrayList<>();

    public List<Vacancy> getVacancies(Filter filter) {
        //TODO как сделать, чтоб возвращал ВСЕ найденные по фильтрам вакансии, а не только 10 или 20,
        // и еще, чтобы работал с разными словами в заголовке вакансии, в том числе с написанными на русском
        String url = "https://api.hh.ru/vacancies?text=" + filter.getTitle() + "&area=" + filter.getCityId() +
                "&per_page=" + filter.getPageNumber() + "&page=199";
        StringBuilder response = null;
        try {
            obj = new URL(url);

            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;

            response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }

        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
        JsonArray vacanciesArray = jsonObject.get("items").getAsJsonArray();

        Gson gson = new Gson();

        for (JsonElement jsonElement : vacanciesArray) {
            Vacancy vacancy = gson.fromJson(jsonElement.getAsJsonObject(), Vacancy.class);
            vacancyList.add(vacancy);

            //System.out.println(vacancy.getId() + " - " + vacancy.getName()); //это тоже не надо здесь
        }

        //System.out.println("Vacancies count: " + vacancyList.size()); //это здесь не надо
        return vacancyList;
    }

    public int getResponses() {
        return 0;
    }
}
