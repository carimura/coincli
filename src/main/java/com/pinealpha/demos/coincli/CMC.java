package com.pinealpha.demos.coincli;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CMC {

  public static List<JSONObject> getCoinList(String start, String limit) throws Exception {
    var client = new OkHttpClient();

    var urlBuilder = HttpUrl.parse("https://pro-api.coinmarketcap.com/v1/cryptocurrency/listings/latest").newBuilder();
    urlBuilder.addQueryParameter("start", start);
    urlBuilder.addQueryParameter("limit", limit);
    urlBuilder.addQueryParameter("convert", "USD");

    var request = new Request.Builder()
        .url(urlBuilder.build().toString())
        .header("X-CMC_PRO_API_KEY", System.getenv("CMC_KEY"))
        .addHeader("Accept", "application/json")
        .build();

    var res = client.newCall(request).execute();
    if (!res.isSuccessful()) {
      closeConnection(res, client);
      throw new RuntimeException(res.toString());
    }
    var respString = res.body().string();
    var results = new JSONObject(respString);
    var ja = results.getJSONArray("data");
    var currencies = new ArrayList<JSONObject>();

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
    var it = list.iterator();
    var c = new JSONObject();
    System.out.printf("Found %s currencies that match: \n", list.size());
    while (it.hasNext()) {
      c = it.next();
      var name = c.getString("symbol");
      var percentChange7d = c.getJSONObject("quote")
          .getJSONObject("USD")
          .getFloat("percent_change_7d");
      var movement = percentChange7d > 0 ? "UP" : "DOWN";
      System.out.printf("%s is %s by %.2f%%\n", name, movement, percentChange7d);
    }
  }
}
