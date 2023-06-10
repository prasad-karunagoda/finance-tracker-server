package prasad.app.finance.tracker.server;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Iterator;
import java.util.Map;

public class AccountsMapperTest {

    @Test
    void testFillIndexToColumnNameMap() {
        Cell mockCell = Mockito.mock(Cell.class);
        Mockito.when(mockCell.getColumnIndex()).thenReturn(0).thenReturn(1).thenReturn(2);
        Mockito.when(mockCell.getStringCellValue()).thenReturn("C1").thenReturn("C2").thenReturn("C3");

        Iterator mockIterator = Mockito.mock(Iterator.class);
        Mockito.when(mockIterator.hasNext()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        Mockito.when(mockIterator.next()).thenReturn(mockCell);

        XSSFRow firstRow = Mockito.mock(XSSFRow.class);
        Mockito.when(firstRow.iterator()).thenReturn(mockIterator);

        XSSFSheet accountsSheet = Mockito.mock(XSSFSheet.class);
        Mockito.when(accountsSheet.getRow(Mockito.anyInt())).thenReturn(firstRow);

        AccountsMapper accountsMapper = new AccountsMapper();
        accountsMapper.fillIndexToColumnNameMap(accountsSheet);
        Map<Integer, String> indexToColumnName = accountsMapper.getIndexToColumnName();
        Assertions.assertEquals(3, indexToColumnName.size());
        Assertions.assertEquals("C1", indexToColumnName.get(0));
        Assertions.assertEquals("C2", indexToColumnName.get(1));
        Assertions.assertEquals("C3", indexToColumnName.get(2));
    }

    @Test
    void testFillIndexToColumnNameMap_EmptySheet() {
        XSSFSheet accountsSheet = Mockito.mock(XSSFSheet.class);
        Mockito.when(accountsSheet.getRow(Mockito.anyInt())).thenReturn(null);

        AccountsMapper accountsMapper = new AccountsMapper();
        accountsMapper.fillIndexToColumnNameMap(accountsSheet);
        Map<Integer, String> indexToColumnName = accountsMapper.getIndexToColumnName();
        Assertions.assertEquals(0, indexToColumnName.size());
    }
}
