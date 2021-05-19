# Gold Board

This application pulls current spot prices of various precious metals from a third party API (currently https://www.goldapi.io).
The price is processed using a pay rate set by the user, and broken down into various purities.
The data is then placed a single html page for easy reference.

<p align="center">
<img width="250" alt="" src="https://user-images.githubusercontent.com/64601713/118853240-66853100-b888-11eb-90ce-34be94a4ef31.png">
</p>

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
