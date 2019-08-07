package com.pinealpha.demos.coincli;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CMC {

  public static Coin[] getCoinList(String start, String limit) throws Exception {
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

    ObjectMapper mapper = new ObjectMapper();

    Result result = mapper.readValue(respString, Result.class);

    Coin[] coins = result.data;

    closeConnection(res, client);

    return coins;
  }

  private static void closeConnection(Response res, OkHttpClient client) {
    res.close();
    client.dispatcher().executorService().shutdown();
    client.connectionPool().evictAll();
  }

  public static void printList(List<Coin> list) {
    Iterator it = list.iterator();
    Coin c = new Coin();
    System.out.printf("Found %s currencies that match: \n\n", list.size());

    System.out.println("\nCOIN\t\t\t\t\t CURRENT PRICE \t 7-DAY CHANGE\n");

    while (it.hasNext()) {
      c = (Coin)it.next();
      Quote q = c.quote.get("USD");
      System.out.printf("%-20s [%s]\t\t $%7.2f \t %6.2f%% \n", c.name, c.symbol, q.price, q.percent_change_7d);
    }
  }

}
