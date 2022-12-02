# Stock Market API Service

Expose an API endpoint where I can POST my data and sign up for an API key that will be used later.
Expose an API endpoint where I can hit and get stock market information, as a security mechanism use the API key obtained previously in order to validate user and make sure that no authorized user will consume the service (use request header for that purpose).

Here are some examples of stock symbols
- Facebook (FB)
- Apple (AAPL)
- Microsoft (MSFT)
- Google (GOOGL)
- Amazon (AMZN)

The system will make use of a web service called Alpha Vantage, this will provide stock market information.

Information that will be retrieved in the response of the service as json format will contain:
- Open price
- Higher price
- Lower price
- Variation between last 2 closing price values.

**Alpha Vantage API**
```
https://www.alphavantage.co/documentation/
API Key: X86NOH6II01P7R24
```

API call sample to get stock prices from Facebook:

`https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=FB&outputsize=compact&apikey=X86NOH6II01P7R24`

**Considerations:**
- URL structure is up to you.
- Initial data for sign up: name, last name, email.
- Validation rules for signup data are up to you.
- Json structure is up to you.
- It will be a big plus if you deploy the services somewhere in the cloud (heroku, gcloud, aws, azure, etc). It's ok if you just do it locally.
- Use github (or other git repo).
- Programming language: Java with spring boot framework
- BONUS: If you can implement API throttling, that's a big one. Throttling rules are up to you (1 API call per second allowed or 10 API calls per minute, etc).
- Log every API call received, log format is up to you.

**No frontend development is required, challenge will be reviewed using postman**


## Current working endpoints examples

### GetApiKey

Request:
````
curl --location --request POST 'localhost:9090/api/stock-market/api-key' \
--header 'Content-Type: application/json' \
--data-raw '{
    "name": "Leslie",
    "lastname": "Nielsen",
    "email": "lnielsen@gmail.com"
}'
````
Response:
````
{
    "apikey": "X86NOH6II01P7R24"
}
````
### Fetch stock prices intraday

With apikey obtained in previous step

Request:
````
curl --location --request GET 'localhost:9090/api/stock-market/price-intraday?function=TIME_SERIES_INTRADAY&symbol=IBM&interval=30min&key=X86NOH6II01P7R24' \
--data-raw ''
````
Response:
````
{
    "metadata": {
        "information": "Intraday (30min) open, high, low, close prices and volume",
        "symbol": "IBM",
        "lastRefreshed": "2022-11-30T20:00:00",
        "interval": "30min",
        "outputSize": "Compact",
        "timezone": "US/Eastern"
    },
    "timeSeries": [
        {
            "dateTime": "2022-11-30T20:00:00",
            "variations": {
                "open": 149.0300,
                "high": 149.0300,
                "low": 149.0300,
                "close": 149.0300,
                "volume": 653
            }
        },
        {
            "dateTime": "2022-11-30T18:30:00",
            "variations": {
                "open": 149.5500,
                "high": 149.5500,
                "low": 149.5500,
                "close": 149.5500,
                "volume": 421
            }
        },
    ]
}
````
