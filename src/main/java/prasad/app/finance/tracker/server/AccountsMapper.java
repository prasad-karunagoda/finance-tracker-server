package prasad.app.finance.tracker.server;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import prasad.app.finance.tracker.server.dto.Account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class AccountsMapper {

    private final Map<Integer, String> indexToColumn = new HashMap<>();
    private final Map<String, AttributeSetter<Account>> columnToSetter = new HashMap<>();

    AccountsMapper() {
        columnToSetter.put("Name", (account, value) -> account.setName((String) value));
        columnToSetter.put("Account Type", (account, value) -> account.setAccountType((String) value));
    }

    List<Account> map(XSSFSheet accountsSheet) {
        XSSFRow firstRow = accountsSheet.getRow(accountsSheet.getFirstRowNum());
        for (Cell header : firstRow) {
            indexToColumn.put(header.getColumnIndex(), header.getStringCellValue());
        }
        System.out.println("FirstCellNum = " + firstRow.getFirstCellNum()); //TODO Use logging

        List<Account> accounts = new ArrayList<>();
        for (Row row : accountsSheet) {
            if (row.getRowNum() > accountsSheet.getFirstRowNum()) {
                Account account = new Account();
                for (Cell cell : row) {
                    String column = indexToColumn.get(cell.getColumnIndex());
                    AttributeSetter<Account> setter = columnToSetter.get(column);
                    if (setter == null) {
                        throw new AttributeSetterMissingException("Attribute setter missing for column: " + column);
                    }
                    setter.set(account, cell.getStringCellValue());
                }
                accounts.add(account);
            }
        }
        return accounts;
    }
}
