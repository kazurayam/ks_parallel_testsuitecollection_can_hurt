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

Here is the timeout value 30 seconds hard-coded.

So, if I can change this `30000` to `5000`, then the Smart Wait will expire more quickly.

## Solution implemented.

- I made a copy of the original into [wait_fast.js](https://github.com/kazurayam/ks_parallel_testsuitecollection_can_hurt/blob/master/src/js/wait_fast.js)
- In the `wait_fast.js`, I manually edit the source to change 30000 to 5000
- In the [`Test Cases/sub/visitUrl`](https://github.com/kazurayam/ks_parallel_testsuitecollection_can_hurt/blob/master/Scripts/sub/visitUrl/Script1709334034155.groovy), the script will load the `wait_fast.js` file into a string, then call `WebUI.executeJavaScript(string)` keyword to send the JavaScript code into the target webpage on the browser runtime
- the browser will execute the `wait_fast.js` script. Effectively the value of `window.katalonWaiter` property will be overwritten.

>I am not very sure if I can overwrite the `window.katalonWaiter` property on time.


## Evaluation

I measured the time duration of the test case TC0 in 2 setups: Smart Wait of 30seconds timeout, and 5 secons timeout. The following is the result.

|Test Case|Duration(secs) with timeout 30secs|Duration(secs) with timeout 5secs|
|---------|-----|-----|
|TC0      | 36  | 24  |

Obviously I saw the `TC0` ran much faster when the Smart Wait timeout is tuned shorter.

## Conclustion

It is just unfortunate that the timeout of Smart Wait is fixed to be 30 seconds; Katalon does not offer any customizability. Now I showed you how to trick KS. I hope this trick may help you sometime.


