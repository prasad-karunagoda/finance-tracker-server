package prasad.app.finance.tracker.server;

import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.springframework.stereotype.Component;
import prasad.app.finance.tracker.server.dto.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
class AccountsMapper {

    private final Map<Integer, String> indexToColumnName = new HashMap<>();
    private final Map<String, AttributeSetter<Account>> columnNameToSetter = new HashMap<>();

    AccountsMapper() {
        columnNameToSetter.put("Name", (account, value) -> account.setName((String) value));
        columnNameToSetter.put("Account Type", (account, value) -> account.setAccountType((String) value));
    }

    List<Account> map(XSSFSheet accountsSheet) {
        fillIndexToColumnNameMap(accountsSheet);

        List<Account> accounts = new ArrayList<>();
        for (Row row : accountsSheet) {
            if (row.getRowNum() > accountsSheet.getFirstRowNum()) {
                Account account = new Account();
                for (Cell cell : row) {
                    String columnName = indexToColumnName.get(cell.getColumnIndex());
                    AttributeSetter<Account> setter = columnNameToSetter.get(columnName);
                    if (setter == null) {
                        throw new AttributeSetterMissingException("Attribute setter missing for column: " + columnName);
                    }
                    setter.set(account, cell.getStringCellValue());
                }
                accounts.add(account);
            }
        }
        return accounts;
    }

    void fillIndexToColumnNameMap(XSSFSheet accountsSheet) {
        XSSFRow firstRow = accountsSheet.getRow(accountsSheet.getFirstRowNum());
        if (firstRow == null) {
            log.warn("Accounts sheet is empty.");
            return;
        }

        for (Cell header : firstRow) {
            indexToColumnName.put(header.getColumnIndex(), header.getStringCellValue());
        }
        log.info("Accounts sheet columns: " + String.join(", ", indexToColumnName.values()));
    }

    Map<Integer, String> getIndexToColumnName() {
        return indexToColumnName;
    }
}
