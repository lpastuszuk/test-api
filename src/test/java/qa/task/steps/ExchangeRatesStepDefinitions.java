package qa.task.steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.path.xml.XmlPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import qa.task.context.TestContext;

import java.util.List;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class ExchangeRatesStepDefinitions {
    final Logger logger = LoggerFactory.getLogger(ExchangeRatesStepDefinitions.class);
    private static final String BASE_URI = "http://api.nbp.pl/api/";
    private static final String EXCHANGE_RATES_RESOURCE = "exchangerates/tables/A";
    private static final String TABLE_PATH = "ArrayOfExchangeRatesTable.ExchangeRatesTable.Table";
    private static final String EFFECTIVE_DATE_PATH = "ArrayOfExchangeRatesTable.ExchangeRatesTable.EffectiveDate";
    private static final String RATE_PATH = "ArrayOfExchangeRatesTable.ExchangeRatesTable.Rates.Rate";
    private final TestContext testContext;

    public ExchangeRatesStepDefinitions(TestContext context) {
        this.testContext = context;
    }

    @Given("the exchange rates from table A")
    public void the_exchange_rates_from_table_a() {
        RestAssured.baseURI = BASE_URI;

        Response response = given().accept("application/xml")
                .when().get(EXCHANGE_RATES_RESOURCE)
                .then().statusCode(200).log().status().extract().response();

        XmlPath xmlPath = new XmlPath(response.asString());
        testContext.setXmlPath(xmlPath);
        logger.atInfo().setMessage("Table: '{}', EffectiveDate: '{}'")
                .addArgument(xmlPath.getString(TABLE_PATH))
                .addArgument(xmlPath.getString(EFFECTIVE_DATE_PATH)).log();
    }

    @Then("view exchange rate for currency code {string}")
    public void view_exchange_rate_for_currency_code(String currencyCode) {
        String exchangeRate = testContext.getXmlPath()
                .getString(RATE_PATH + ".find { it.Code == '" + currencyCode + "' }.Mid");

        assertThat(exchangeRate).isNotEmpty().contains(".");
        logger.atInfo().setMessage("Exchange rate for '{}': '{}'")
                .addArgument(currencyCode).addArgument(exchangeRate).log();
    }

    @Then("view exchange rate for currency name {string}")
    public void view_exchange_rate_for_currency_name(String currencyName) {
        String exchangeRate = testContext.getXmlPath()
                .getString(RATE_PATH + ".find { it.Currency == '" + currencyName + "' }.Mid");

        assertThat(exchangeRate).isNotEmpty().contains(".");
        logger.atInfo().setMessage("Exchange rate for '{}': '{}'")
                .addArgument(currencyName).addArgument(exchangeRate).log();
    }

    @Then("view currencies with exchange rate above {string}")
    public void view_currencies_with_exchange_rate_above(String rate) {
        List<String> currencies = testContext.getXmlPath()
                .getList(RATE_PATH + ".findAll { it.Mid.toFloat() > " + rate + " }.Code");

        assertThat(currencies.isEmpty()).isFalse();
        logger.atInfo().setMessage("Currencies with exchange rate above '{}': '{}'")
                .addArgument(rate).addArgument(currencies).log();
    }

    @Then("view currencies with exchange rate below {string}")
    public void view_currencies_with_exchange_rate_below(String rate) {
        List<String> currencies = testContext.getXmlPath()
                .getList(RATE_PATH + ".findAll { it.Mid.toFloat() < " + rate + " }.Code");

        assertThat(currencies.isEmpty()).isFalse();
        logger.atInfo().setMessage("Currencies with exchange rate below '{}': '{}'")
                .addArgument(rate).addArgument(currencies).log();
    }
}
