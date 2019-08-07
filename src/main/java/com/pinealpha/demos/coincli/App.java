package com.pinealpha.demos.coincli;

import org.apache.commons.cli.*;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class App {

  public static void main(String[] args) throws Exception {

    if (System.getenv("CMC_KEY") == null) {
      System.err.println("Invalid or missing CoinMarketCap API key. Set environment variable CMC_KEY to your API key.");
      System.exit(1);
    }

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

      Coin[] coins = CMC.getCoinList("1", limit);

      List<Coin> currencies = Arrays.asList(coins);

      Predicate<String> p = (s) -> s.startsWith(coin);

      if (cli.hasOption("up")) {
        currencies = currencies
            .stream()
            .filter(a -> a.quote.get("USD").percent_change_7d > 0)
            .sorted(Comparator.comparing(a -> a.quote.get("USD").percent_change_7d))
            .collect(Collectors.toList());
      }

      if (cli.hasOption("down")) {
        currencies = currencies
            .stream()
            .filter(a -> a.quote.get("USD").percent_change_7d < 0)
            .sorted(Comparator.comparing(a -> a.quote.get("USD").percent_change_7d))
            .collect(Collectors.toList());
      }

      CMC.printList(currencies);

    } catch (UnrecognizedOptionException e) {
      System.err.println(e.getMessage());
    }
  }
}

