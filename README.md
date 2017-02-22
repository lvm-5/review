# review
To compile project and run JUnit tests use "gradle clean build". JUnit reports see here ../review/build/reports/tests/test/index.html
To run application to show 1,2,3 of the chelange use "gradle runReviewAnalyzing"
To run application to perform 4 additionaly use "gradle runWS" (accessable at "http://api.google.com:8080" that mapped to local host) and use new terminal to run "gradle runReviewAnalyzingWithTranslate". 
I know there are things that could be implemented better, so if it's actual to you I can 
- improve memory management, 
- improve final part of translating,
- differentiate requests for web service

P.S. I didn't commite Reviews.csv file due to it's a huge. So please place it in the root project directory before the application is run.
