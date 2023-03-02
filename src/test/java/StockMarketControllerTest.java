import com.eurekalabs.stockmarketapi.controller.StockMarketController;
import com.eurekalabs.stockmarketapi.error.InternalErrorException;
import com.eurekalabs.stockmarketapi.error.UserValidationException;
import com.eurekalabs.stockmarketapi.model.Price;
import com.eurekalabs.stockmarketapi.service.StockMarketService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class StockMarketControllerTest {

    @InjectMocks
    private StockMarketController controller;

    @Mock
    private StockMarketService service;

    private String apiKey;
    private String function;
    private String symbol;
    private String interval;

    @BeforeEach
    public void setup() {
        // Set up test data
        apiKey = "test-api-key";
        function = "test-function";
        symbol = "test-symbol";
        interval = "test-interval";
    }

    @Test
    public void testGetStockMarket() throws Exception {

        // Set up the mock service response
        Price expectedPrice = new Price();
        when(service.getPriceIntraday(apiKey, function, symbol, interval)).thenReturn(expectedPrice);

        // Call the method under test
        ResponseEntity<?> responseEntity = controller.getStockPrice(apiKey, function, symbol, interval);

        // Verify the result
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPrice, responseEntity.getBody());
    }

    @Test
    public void testGetStockMarket_ValidationException() throws Exception {
        String validationErrorMessage = "Invalid input";

        // Set up the mock service response
        UserValidationException validationException = new UserValidationException(validationErrorMessage);
        when(service.getPriceIntraday(apiKey, function, symbol, interval)).thenThrow(validationException);

        // Call the method under test
        ResponseEntity<?> responseEntity = controller.getStockPrice(apiKey, function, symbol, interval);

        // Verify the result
        assertEquals(HttpStatus.UNAUTHORIZED, responseEntity.getStatusCode());
        assertEquals(validationErrorMessage, responseEntity.getBody());
    }

    @Test
    public void testGetStockMarket_InternalServerError() throws Exception {
        String errorMessage = "Internal server error";

        // Set up the mock service response
        InternalErrorException internalErrorException = new InternalErrorException(errorMessage);
        when(service.getPriceIntraday(apiKey, function, symbol, interval)).thenThrow(internalErrorException);

        // Call the method under test
        ResponseEntity<?> responseEntity = controller.getStockPrice(apiKey, function, symbol, interval);

        // Verify the result
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertEquals(errorMessage, responseEntity.getBody());
    }
}
