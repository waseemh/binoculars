# Sherlock

Sherlock is an open source tool for automating visual regression tests using Selenium WebDriver. Thanks to its simple API, Sherlock can be semeassly integrated into your existing Selenium tests.

##How it Works

On its first run, Sherlock saves a baseline screenshot for all elements that need to be visually tested. Consecutive runs will capture a new screenshot for each element and compare it with its corresponding baseline.
Using its image comparison engine, Sherlock will compare the screenshots and fail the test if mismatch occurs.
