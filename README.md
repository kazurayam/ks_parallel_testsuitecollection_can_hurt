# [Katalon Studio] Parallel mode of Test Suite Collection --- is it faster than Sequential mode

## Problem to solve

Katalon Studio provides a feature [Parallel execution of Test Suite Collection](https://docs.katalon.com/docs/katalon-studio/execute-tests/execute-test-suite-collections-in-katalon-studio#parallel-mode). Does it make your test project run faster than the sequential mode?

I did a study about this question. I would present you a data.

## Environment

I used Chrome v122, Katalon Studio v9.0.0 with appropriate version of ChromeDriver manually upgraded.

I set the SmartWait ON.

I specified a larger memroy size to Katalon JVM (-Xmx4096m) than the default 2048m.

## How to run the exam

Run `Test Suites/TSC_parallel` and `Test Suites/TSC_sequential`.

then you will see 3 Chrome windows are opened in parallel:

![parallel_TSC](https://kazurayam.github.io/ks_parallel_testsuitecollection_can_hurt/images/parallel_TSC.png)

You can see how the Test Suite Collection runs.

## A study of speed

Question: How long it takes to run the Test Suite Collection in sequential mode and in parallel mode? Does the parallel mode run faster than the sequential mode?

### Speed of Test Cases

I used Chrome browser to run the test cases with.

How long each Test Cases takes to run?

|Test Suite|seconds|Test Case|seconds| visits which URL?        |
|----------|-------|---------|-------|--------------------------|
|TS0       | 24    |TC0      | 23    |https://kyoto.travel/en/  |
|TS1       | 16    |TC1      | 14    |https://www.esbnyc.com/   |
|TS2       | 14    |TC2      | 13    |https://www.louvre.fr/en/ |

The sum of seconds of TS0 + TS1 + TS2 makes 54 seconds

### Speed of Sequential execution

How long the `Test Suites/TSC_sequential` took to finish executing all 3 Test Cases?

I used my stockwatch device to measure it.

I got the figure: 84 seconds.


### Speed of Parallel execution

How long the `Test Suites/TSC_parallel` took to finish executing the same set?

I set the number of parallel execution to be 3.

I got the figure: 34 seconds.

