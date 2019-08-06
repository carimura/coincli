# CoinCLI

This is a simple CLI tool that gets cryptocurrency prices from
CoinMarketCap. It's written in Java and compiled ahead-of-time into 
an executable (as CLI's should be) using GraalVM's native-image stuff.

You probably don't want to use this to perform technical analysis. 
Or at all really. But here it is.

![map to riches](https://imgs.xkcd.com/comics/technical_analysis_2x.png)

# Using

- Just type ./coincli for the man page

```
❯ ./coincli

usage: coincli
 -c,--coin <arg>   filter on a specific coin
 -d,--down         return only coins that are down over specified time
                   [default: false]
 -h,--help         see all coincli help page
 -n,--num <arg>    max number of coins to return [default: 1000]
 -u,--up           return only coins that are up over specified time
                   [default: false]
```

# Building

## Pre-Requisites

- [GraalVM CE 19](https://github.com/oracle/graal/releases)
- Native Image: `gu install native-image` (see [here](https://www.graalvm.org/docs/reference-manual/aot-compilation/))

## And then...

1. `mvn clean build`
2. `native-image -jar target/coincli-0.1-jar-with-dependencies.jar coincli`


# Making Lots of Money*

Step 1. Use CoinCLI

Step 2. ???

Step 3. Riches. 

\* this is not investment or legal advice.