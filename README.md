# [Katalon Studio] Parallel mode of Test Suite Collection --- is it faster than Sequential mode

## Problem to solve

Katalon Studio provides a feature [Parallel execution of Test Suite Collection](https://docs.katalon.com/docs/katalon-studio/execute-tests/execute-test-suite-collections-in-katalon-studio#parallel-mode). Does it make your test project run faster than the sequential mode?

I did a study about this question. I would report the result here.

## How to run the exam

Run `Test Suites/TSC_parallel` and `Test Suites/TSC_sequential`.

then you will see 4 Chrome windows are opened in parallel:

![parallel_TSC](https://kazurayam.github.io/ks_parallel_testsuitecollection_can_hurt/images/parallel_TSC.png)

You can see how the Test Suite Collection runs.

## A study of speed

Question: How long it takes to run the Test Suite Collection in sequential mode and in parallel mode? Does the parallel mode run faster than the sequential mode?

### Speed of Test Cases

How long each Test Cases takes to run?

|Test Case| visits which URL?         | approx seconds|
|---------|---------------------------|----|
|TC0      | https://kyoto.travel/en/  | 22 |
|TC1      | https://www.esbnyc.com/   | 13 |
|TC2      | https://www.louvre.fr/en/ | 13 |
|TC3      | https://warsawtour.pl/en/ | 41 |

I set the SmartWait ON.

Obviously, the sum of seconds of TC0 + TC1 + TC2 + TC3 makes approximately 90 seconds

### Speed of Sequential execution

How long the `Test Suites/TSC_sequential` took to finish executing all 4 Test Cases?

I used my stockwatch device to measure it.

I got the figure: 180 seconds.


### Speed of Parallel execution

How long the `Test Suites/TSC_parallel` took to finish executing the same set?

I set the number of parallel execution to be 4.

I got the figure: 127 seconds.

## What I observed

I chose 4 URL as target to visit. I had no particular reason why I chose these 4. They are public URL that advertise famous site-seeing locations.

Some of them are using AJAX technology, they have ever-moving UI components. This causes technical challenges for Selenium-based automation tools to determine when the page fully loaded. The movig UI components confuse selenium-based tests. Katalon Studio provides a feature named "Smart Wait", which handles the AJAX-driven events in the page and let the test scripts wait the page loading for atmost 30 seconds, stop waiting and go to the next steps gracefully.

The Sequential mode took long time. It took 180 seconds whereas the sum of the composing Test cases is 90. It took longer than the sum of the composing Test Cases, of course. The overhead of launching browser processes added more duration.

The Parallel mode also took long time. It took 120 seconds, which is longer the simple sum of Test Cases 90.

Still the parallel mode ran quicker than the sequential mode. Why? The long wait for the page loading caused this duration difference. In the parallel mode, in the 4 windows of browsers, I could observe that all 4 test scripts were waiting for the pages to finsih loading. **The scripts were waiting parallely.** So, the Test Suite Collection of Parallel mode finished as soon as the slowest member Test Suite finished while the rest had finished beforehand.

A warning: this examination may give you a wrong impression: *In the example, the Parallel mode took 127 seconds whereas the Sequential mode took 180 seconds. So the parallel mode is faster!*

I would argue it is not quite right. The most important factor that determined the speed in this examination was the nature of each target URLs: how long each Test Case had to wait for the pages to load completely.

## Conclusion

I think it is pointless to discuess if we should challenge any parallel execution of test scripts, or not. Rather we should look into the nature of each indivisual target URLs and try to minimize the unnecessary wait in each test scripts. The speed of each test cases is the most important factor for total duration. You should make every efforts to tune each test cases run at the maximum speed. You shouldn't hope for the Parallel execution to do you a magic.
