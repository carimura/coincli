package com.pinealpha.demos.coincli;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CMC {

  public static List<JSONObject> getCoinList(String start, String limit) throws Exception {
    OkHttpClient client = new OkHttpClient();

    HttpUrl.Builder urlBuilder = HttpUrl.parse("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest").newBuilder();
    urlBuilder.addQueryParameter("start", start);
    urlBuilder.addQueryParameter("limit", limit);
    urlBuilder.addQueryParameter("convert", "USD");

    Request request = new Request.Builder()
        .url(urlBuilder.build().toString())
        .header("X-CMC_PRO_API_KEY", System.getenv("CMC_KEY"))
        .addHeader("Accept", "application/json")
        .build();

    Response res = client.newCall(request).execute();
    if (!res.isSuccessful()) {
      closeConnection(res, client);
      throw new RuntimeException(res.toString());
    }
    String respString = res.body().string();
    JSONObject results = new JSONObject(respString);
    JSONArray ja = results.getJSONArray("data");
    ArrayList<JSONObject> currencies = new ArrayList<>();

    for (int i = 0; i < ja.length(); i++) {
      currencies.add(ja.getJSONObject(i));
    }

    closeConnection(res, client);

    return currencies;
  }

  private static void closeConnection(Response res, OkHttpClient client) {
    res.close();
    client.dispatcher().executorService().shutdown();
    client.connectionPool().evictAll();
  }

  public static void printList(List<JSONObject> list) {
    Iterator it = list.iterator();
    JSONObject c = new JSONObject();
    System.out.printf("Found %s currencies that match: \n\n", list.size());

    System.out.println("\nCOIN\t CURRENT PRICE \t 7-DAY CHANGE\n");
    while (it.hasNext()) {
      c = (JSONObject)it.next();
      String name = c.getString("symbol");
      Float price = c.getJSONObject("quote")
          .getJSONObject("USD")
          .getFloat("price");
      Float percentChange7d = c.getJSONObject("quote")
          .getJSONObject("USD")
          .getFloat("percent_change_7d");
      System.out.printf("%s\t $%7.2f \t %6.2f%% \n", name, price, percentChange7d);
    }
  }
}
