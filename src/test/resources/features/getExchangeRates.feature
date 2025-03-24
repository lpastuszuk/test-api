Feature: Get exchange rates from table A
  API should return the exchange rates from table A

  Scenario: Get exchange rates from table A
    Given the exchange rates from table A
    Then view exchange rate for currency code "USD"
    Then view exchange rate for currency name "dolar amerykański"
    Then view currencies with exchange rate above "5"
    Then view currencies with exchange rate below "3"