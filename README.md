# CoinCLI

This is a simple CLI tool that gets cryptocurrency prices from
CoinMarketCap. It's written in Java and compiled ahead-of-time into 
an executable (as CLI's should be) using GraalVM's native-image stuff.

You probably don't want to use this to become a day trader. 
Or at all really. But here it is.

# Pre-Requisites

- [GraalVM CE 19](https://github.com/oracle/graal/releases)
- Native Image: `gu install native-image` (see [here](https://www.graalvm.org/docs/reference-manual/aot-compilation/))

# Building

1. `mvn clean build`
2. `native-image -jar target/coincli-0.1-jar-with-dependencies.jar coincli`

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

# Making Lots of Money*

Be a trader. Here's the map.

![map to riches](https://imgs.xkcd.com/comics/technical_analysis_2x.png)


\* this is not investment or legal advice.