package prasad.app.finance.tracker.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import prasad.app.finance.tracker.server.dto.Account;

import java.util.List;

@RestController
@RequestMapping("/rest/finance-tracker")
public class FinanceTrackerController {

    @Autowired
    private ExcelFile excelFile;

    @GetMapping
    public ResponseEntity<List<Account>> getAccounts() {
        return new ResponseEntity<>(excelFile.getAccounts(), HttpStatus.OK);
    }
}
