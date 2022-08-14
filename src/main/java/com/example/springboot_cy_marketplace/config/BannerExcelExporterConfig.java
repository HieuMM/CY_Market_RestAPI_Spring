package com.example.springboot_cy_marketplace.config;

import com.example.springboot_cy_marketplace.entity.OrderEntity;
import com.example.springboot_cy_marketplace.entity.UserEntity;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.util.List;


public class BannerExcelExporterConfig {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    private List<OrderEntity> orderEntityList;


    public BannerExcelExporterConfig(List<OrderEntity> orderEntityList) {
        this.orderEntityList = orderEntityList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Profit static");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);
        style.setFillBackgroundColor(IndexedColors.BLUE_GREY.getIndex());

        createCell(row, 0, "Mã đơn hàng", style);
        createCell(row, 1, "Ngày tạo", style);
        createCell(row,2 , "Giá nhập", style);
        createCell(row, 3, "Doanh thu", style);
        createCell(row, 4, "Lợi nhuận", style);
        createCell(row, 5, "Payment", style);
        createCell(row, 6, "Shipping", style);
        createCell(row, 7, "Trạng thái", style);
        createCell(row, 8, "Địa chỉ", style);
        createCell(row, 9, "Ghi chú", style);

    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Long) {
            cell.setCellValue((Long) value);
        } else if (value instanceof Double) {
            cell.setCellValue((Double) value);
        } else if (value instanceof Date) {
            cell.setCellValue((Date) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataLines() {
        int rowCount = 1;
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);
        for (OrderEntity orderEntity : orderEntityList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;
            createCell(row, columnCount++, orderEntity.getId(), style);
            createCell(row, columnCount++, orderEntity.getCreateDate().toString(), style);
            createCell(row, columnCount++, orderEntity.getCostPrice(), style);
            createCell(row, columnCount++, orderEntity.getTotalPrice(), style);
            createCell(row, columnCount++, orderEntity.getProfit(), style);
            createCell(row, columnCount++, orderEntity.getPaymentMethod(), style);
            createCell(row, columnCount++, orderEntity.getShippingFee(), style);
            createCell(row, columnCount++, orderEntity.getStatus(), style);
            createCell(row, columnCount++, orderEntity.getHomeAddress() +","+ orderEntity.getDistrictName() +","+ orderEntity.getCityName(), style);
            createCell(row, columnCount++, null, style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderLine();
        writeDataLines();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();

        outputStream.close();

    }
}
