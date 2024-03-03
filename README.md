# [Katalon Studio] Parallel mode of Test Suite Collection --- is it faster than Sequential mode

## Problem to solve

Katalon Studio provides a feature [Parallel execution of Test Suite Collection](https://docs.katalon.com/docs/katalon-studio/execute-tests/execute-test-suite-collections-in-katalon-studio#parallel-mode). Does it make your test project run faster than the sequential mode?

I did a study about this question. I would present you a data.

## Environment

I used Katalon Studio v9.0.0 on Mac, Chrome v122, manually upgraded ChromeDriver to the appropriate version.

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

How long each Test Cases takes to run?

|Test Case| visits which URL?         | approx seconds|
|---------|---------------------------|----|
|TC0      | https://kyoto.travel/en/  | 36 |
|TC1      | https://www.esbnyc.com/   | 19 |
|TC2      | https://www.louvre.fr/en/ | 14 |

The sum of seconds of TC0 + TC1 + TC2 makes approximately 70 seconds

### Speed of Sequential execution

How long the `Test Suites/TSC_sequential` took to finish executing all 3 Test Cases?

I used my stockwatch device to measure it.

I got the figure: 130 seconds.


### Speed of Parallel execution

How long the `Test Suites/TSC_parallel` took to finish executing the same set?

I set the number of parallel execution to be 3.

I got the figure: 107 seconds.

## What I observed

I chose 3 URL as target to visit without any particular reason. They are public URL that advertise famous site-seeing locations.

One of them (京都) is using JavaScript heavily; it have some visual parts that continue moving. This brings a technical challenge for a Selenium-based UI automation tool to determine when the page fully loaded. The ever-changing DOM confuses selenium-based tests. In order to manage this difficulty, Katalon Studio provides a feature named "Smart Wait", which handles the JavaScript DOM events in the page and let the test scripts wait for the movement to stop. Smart Wait waits for atmost 30 seconds. Once expired, Smart Wait will stop waiting and gracefully let the test script to continue going.

The Sequential mode took long time. It took 130 seconds whereas the sum of the composing Test cases is 70. It took longer than the sum of the composing Test Cases, of course.

The Parallel mode also took long time. It took 107 seconds, which is longer the simple sum of Test Cases 70.

Still the parallel mode ran a bit quicker than the sequential mode. Why? The long wait for the page loading caused this duration difference. In the parallel mode, in the 3 windows of browsers, I could observe that all 3 test scripts were waiting for the pages to finish loading. **The scripts were waiting parallely for long period.** So, the Test Suite Collection of Parallel mode finished as soon as the slowest member Test Suite finished while the rest had finished beforehand.

A warning: this examination may give you a wrong impression: *In the example, the Parallel mode took 107 seconds whereas the Sequential mode took 130 seconds. So the parallel mode is faster!*

I would argue it is not quite right. The most important factor that determined the speed in this examination was the nature of each target URLs: how long each Test Case had to wait for the pages to load completely.

## Conclusion

I think it is pointless to discuss if we should challenge any parallel execution of test scripts, or not. Rather we should look into the nature of each indivisual target URLs and try to minimize the unnecessary wait in each test scripts. The speed of each test cases is the most important factor for total duration. You should make every efforts to tune each test cases run at the maximum speed. You shouldn't hope for the Parallel execution to do you a magic.


