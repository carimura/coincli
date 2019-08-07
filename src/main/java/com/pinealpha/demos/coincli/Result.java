package com.pinealpha.demos.coincli;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
class Result {
  public Coin[] data;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Coin {
  public String name;
  public String symbol;
  public Map<String, Quote> quote;
}

@JsonIgnoreProperties(ignoreUnknown = true)
class Quote {
  public float price;
  public float percent_change_7d;
}