# [Katalon Studio] Parallel mode of Test Suite Collection can hurt

A demonstration.

## How to run

Run `Test Suites/TSC0_parallel` and `Test Suites/TSC1_sequential`.

then you will see 4 Chrome windows are opened in parallel:

![parallel_TSC](https://kazurayam.github.io/ks_parallel_testsuitecollection_can_hurt/images/parallel_TSC.png)

You can see how the Test Suite Collection runs.

Do you like it or not?

Just have a look.

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

127 seconds.

## What I observed

I chose 4 URL as target to visit. I had no particular reason why I chose these 4. They are public URL that advertise famous site-seeing locations.

Some of them are using AJAX technology, they have ever-moving UI components. This causes technical challenges for Selenium-based automation tools to determine when the page fully loaded. The movig UI components confuse selenium-based tests. Katalon Studio provides a feature named "Smart Wait", which handles the AJAX-driven events in the page and let the test scripts wait the page loading for atmost 30 seconds, stop waiting and go to the next steps gracefully.

The Sequential mode took take long time. It took 180 seconds whereas the sum of the composing Test cases is 90. It took longer than the sum of the composing Test Cases, of course. Plus the overhead of launching browser processes added more.

The Parallel mode also took long time. It took 120 seconds, which is longer the simple sum of Test Cases 90.
Still the parallel mode ran quicker than the sequential mode. Why? The long delay of loading pages caused this time gap. In the parallel mode, in the 4 windows of browsers, I could observe that test scripts are just waiting for the page to load. **The scripts were waiting parallely.** So, the Test Suite Collection of Parallel mode finished as soon as the slowest member Test Suite finished while the rest finished beforehand.

This examination may impress you a misunderstanding. You may have got an impression that the Parallel mode of Test Suite Collection can run faster than the Sequential mode because its "parallel". I would say, it is not quite right.

The most important factor that determined the speed in this examination was the nature of the target URLs: how long each Test Case had to wait for the pages to load completely.

## Conclusion

I think it is pointless to discuess if we should challenge any parallel execution of test scripts, or not. Rather we should look into the nature of each indivisual target URLs and try to minimized the wait of page loading.

