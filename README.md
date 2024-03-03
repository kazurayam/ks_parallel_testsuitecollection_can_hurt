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


# Second report: How to change Smart Wait to expire faster in 5 seconds rather than 30 secs

## Problem to solve

When the [`Test Cases/TC0`](https://github.com/kazurayam/ks_parallel_testsuitecollection_can_hurt/blob/master/Scripts/TC0/Script1709339037820.groovy) visits the site https://kyoto.travel/en/ , it takes 36 seconds. Why it takes this long seconds? I want it to run in 20 seconds or so.

It is because this web page uses JavaScript heavily, uses timer-driven DOM manipulation, which continues forever. Because the DOM is continuously modified, for Selenium-based automated tests' point of view, this web page looks to be loading forever. In order to manage the difficulty of page loading, Katalon Studio offers a feature "Smart Wait". See https://katalon.com/resources-center/blog/handle-selenium-wait for detail. The Smart Wait waits for 30 seconds maximum for the DOM-changes to stop. When the timeout expires, Smart Wait returns gracefully to the caller test script as if the page loading has successfully finished. So the test script will be able to continue its processing as if the page has completely loaded.

In the case of `Test Cases/TC0`, the Smart Wait worked in the background. It waited 30 seconds. Therefore `Test Cases/TC0` took 34 secons to finish.

On the other hand, I observe that the [target page](https://kyoto.travel/en/) finishes loading in 5 seconds or so. I find no reazon to wait for 30 seconds. I want Smart Wait to return more quickly.

I know, Katalon Studio does not offer any option to change the timeout value of Smart Wait. But I want to change the timeout of the Smart Wait from 30 seconds to 5 seconds. Is it possible?

## Solution

I could find the Katlaon Studio's source code that implements the Smart Wait feature.

https://github.com/katalon-studio/katalon-studio-testing-framework/blob/master/Include/scripts/groovy/com/kms/katalon/core/webui/common/internal/SmartWait.java

This Groovy code configures and launch the Smart Wait feature. It executes JavaScript snippet that creates the `window.katalonWaiter` property and register callbacks `katalon_smart_waiter_do_ajax_wait()` and `katalon_smart_waiter_do_dom_wait()`. Then, how the callback functions are implemented?

Months ago, @Russ_thomas pointed me to the `<Katalon Studio Installation dir>\Content\configuration\resources\extensions\Chrome\Smart Wait\content\wait.js`. See the following link to see a copy of the source for reference:

- [wait.js](https://github.com/kazurayam/ks_parallel_testsuitecollection_can_hurt/blob/master/src/js/wait.js)

In this source code I could find the following line:

```
    function katalon_smart_waiter_do_dom_wait() {
      setTimeout(() => {
        if (domTime && (Date.now() - domTime > 30000)) {
        ...
```

Here is the timeout value 30 seconds is hard coded.

So, if I can change this `30000` to `5000`, then the Smart Wait will expire more quickly.

## Solution implemented.

- I made a copy of the original into [wait_fast.js](https://github.com/kazurayam/ks_parallel_testsuitecollection_can_hurt/blob/master/src/js/wait_fast.js)
- In the `wait_fast.js`, I manually edit the source to change 30000 to 5000
- In the [`Test Cases/sub/visitUrl`](https://github.com/kazurayam/ks_parallel_testsuitecollection_can_hurt/blob/master/Scripts/sub/visitUrl/Script1709334034155.groovy), the script will load the `wait_fast.js` file into a string, then call `WebUI.executeJavaScript(string)` keyword to send the JavaScript code into the target webpage on the browser runtime
- the browser will execute the `wait_fast.js` script. Effectively the value of `window.katalonWaiter` property will be overwritten.

## Evaluation

I measured the time duration of the test case TC0 in 2 setups: Smart Wait of 30seconds timeout, and 5 secons timeout. The following is the result.

|Test Case|Duration(secs) with timeout 30secs|Duration(secs) with timeout 5secs|
|---------|-----|-----|
|TC0      | 36  | 24  |

Obviously I saw the `TC0` ran much faster when the Smart Wait timeout is tuned shorter.

## Conclustion

It is just unfortunate that the timeout of Smart Wait is fixed to be 30 seconds; Katalon does not offer any customizability.

Now I showed you how to trick KS. I hope this trick may save you sometime.


