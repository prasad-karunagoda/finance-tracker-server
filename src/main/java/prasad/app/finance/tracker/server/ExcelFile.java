package prasad.app.finance.tracker.server;

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import prasad.app.finance.tracker.server.dto.Account;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;

@Component
public class ExcelFile {

    @Value("classpath:data.xlsx")
    private Resource excelResource;

    private List<Account> accounts;

    private List<Object> records;

    @PostConstruct
    private void load() {
        try (XSSFWorkbook file = new XSSFWorkbook(excelResource.getInputStream())) {
            XSSFSheet accountsSheet = file.getSheet("Accounts");
            accounts = new AccountsMapper().map(accountsSheet);
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (AttributeSetterMissingException ex) {
            ex.printStackTrace();
        }
    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public List<Object> getRecords() {
        return records;
    }
}
