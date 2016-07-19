# ![alt text](https://github.com/waseemh/binoculars/blob/master/binoculars-with-eyes-5.png?raw=true "Logo Title Text 1")

Binoculars is an open source tool for automating visual regression tests for web and mobile, using Selenium WebDriver.

###Features
- Uses Selenium WebDriver API. Can be easily integrated into existing Selenium tests
- Support all Webdriver-backed browsers and mobile devices with screenshot capability
- Uses Resemble.js Java implementation for smart image comparisons algorithm (not pixel by pixel)
- Fully configurable - Each configuration can be modified using Configuration API
- Detailed reports of visual mismatch

##How it Works

On its first run, Binoculars saves a baseline screenshot for all elements that need to be visually tested. Consecutive runs will capture a new screenshot for each element and compare it with the corresponding baseline.
Using its image comparison engine, Binoculars will check for mismatch in screenshots and fail the test if mismatch occurs.

##Getting Started

To initialize Binoculars, you must provide it with a WebDriver object.

``` java
WebDriver driver = new FirefoxDriver();
binoculars = new Binoculars(driver);
```

Binoculars is now ready to go! 

Next step would be start capturing elements in UI. Binoculars will locate elements on screen using WebDriver's By selectors. Each captured element is uniquely identified by its given label. 

In below example, we will capture logo image using ID selector type. We will label this element as "logo", so Binoculars can refer to it in the future.

``` java
binoculars.capture("logo", By.id("uh-logo"));
```

In order to check for regression in a specific element, you can compare an element with its baseline using label:

``` java
binoculars.compare("logo");
```

In order to check all captured elements in a test, you can compare all elements at once:

``` java
binoculars.compareAll();
```

##Configuration
Binoculars is fully configurable. Below is a full list of available configurations:

- rootFolder: root folder for storing all screenshots
- failuresFolder: folder for storing failed screenshots
- screenshotsFolder: folder for storing captured screenshots
- baselineFolder: folder for storing/loading baseline screenshots
- reportFolder: folder for report output
- baselineMode: Force to run in baseline mode, overriding any previous captured screenshots
- compareUponCapture: If set to true, screenshot comparison will be immediately after capturing
- engine: Image comparison engine
- mismatchThreshold: Set threshold for image mismatch percentage. If a comparison result is above mismatch, comparison will be marked as failed
- baselineExtension: Suffix for baseline screenshots filenames
- captureExtension: Suffix for captured screenshots filenames
- diffExtension: Suffix for diff images filenames
- failExtension: Suffix for fail screenshots filenames
- diffColor: Color to use for highlighting mismatch in diff images.
- resourceManager:
- executor:
- handler:
- absorbFailures: If set to true, comparison mismatch will not fail the test (will not throw an AssertionError)
- reporter: 

##Reporting
Bincoulars produces a detailed report of all comparisons. Reports will be generated in these cases:

- A mismatch occurs when comparing a specific element and 'absorbFailures' flag is set to false (by default)
- When comparing all elements at once (by invoking compareAll())
- By directly invoking generateReports() - Note that this operation will override existing reports when called multiple times during test.
