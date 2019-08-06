package com.pinealpha.demos.coincli;

import org.apache.commons.cli.*;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class App {

  public static void main(String[] args) throws Exception {

    Options options = new Options();
    options.addOption("h", "help", false, "see all coincli help page");
    options.addOption("n", "num", true, "max number of coins to return [default: 1000]");
    options.addOption("u", "up", false, "return only coins that are up over specified time [default: false]");
    options.addOption("d", "down", false, "return only coins that are down over specified time [default: false]");
    options.addOption("c", "coin", true, "filter on a specific coin");

    try {
      CommandLineParser parser = new DefaultParser();
      CommandLine cli = parser.parse(options, args);

      HelpFormatter formatter = new HelpFormatter();

      if (cli.getOptions().length == 0) {
        formatter.printHelp("coincli", options);
        System.exit(-1);
      }

      String limit = cli.hasOption("n") ? (String) cli.getOptionValue("n") : "1000";
      System.out.println("limit: " + limit);

      String coin = cli.hasOption("coin") ? cli.getOptionValue("coin").toUpperCase() : "";
      System.out.println("coin: " + coin);

      List<JSONObject> allCurrencies = CMC.getCoinList("1", limit);

      Predicate<String> p = (s) -> s.startsWith(coin);

      List<JSONObject> filteredCurrencies = allCurrencies
          .stream()
          .filter(a -> p.test(a.getString("symbol")))
          .collect(Collectors.toList());

      if (cli.hasOption("up")) {
        filteredCurrencies = filteredCurrencies
            .stream()
            .filter(a -> a.getJSONObject("quote").getJSONObject("USD").getFloat("percent_change_7d") > 0)
            .sorted(Comparator.comparing(a -> a.getJSONObject("quote").getJSONObject("USD").getFloat("percent_change_7d")))
            .collect(Collectors.toList());
      }

      if (cli.hasOption("down")) {
        filteredCurrencies = filteredCurrencies
            .stream()
            .filter(a -> a.getJSONObject("quote").getJSONObject("USD").getFloat("percent_change_7d") < 0)
            .sorted(Comparator.comparing(a -> a.getJSONObject("quote").getJSONObject("USD").getFloat("percent_change_7d")))
            .collect(Collectors.toList());
      }

      CMC.printList(filteredCurrencies);
    } catch (UnrecognizedOptionException e) {
      System.err.println(e.getMessage());
    }
  }
}

