# Gold Board

This application pulls current spot prices of various precious metals from a third party API (currently https://www.goldapi.io).
The price is processed using a pay rate set by the user, and broken down into various purities.
The data is then placed a single html page for easy reference.

## Installation

Set API key inside rate-config file, and build using Maven.
```bash
./mvnw package
```

## Usage

http://localhost:8080

Upon first successful interaction with the API (and each time after) a backup price file will be created that will be used in case the API is unreachable for any reason in the future.

The application will also write debug logs to a newly created logs folder.


## Info
Feedback, advice, kind words are all accepted at any time :)

## License
[MIT](https://choosealicense.com/licenses/mit/)