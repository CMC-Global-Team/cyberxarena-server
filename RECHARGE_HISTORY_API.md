# Recharge History API Documentation

## 📋 Tổng quan
API quản lý lịch sử nạp tiền của khách hàng trong hệ thống quản lý internet cafe.

## 🔗 Base URL
```
/api/recharge-history
```

## 📊 Cấu trúc dữ liệu

### RechargeHistoryDTO
```json
{
  "rechargeId": 1,
  "customerId": 123,
  "amount": 100000.00,
  "rechargeDate": "2024-01-15T10:30:00",
  "customerName": "Nguyễn Văn A"
}
```

### CreateRechargeHistoryRequestDTO
```json
{
  "customerId": 123,
  "amount": 100000.00
}
```

### RechargeHistorySearchRequestDTO
```json
{
  "customerId": 123,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-01-31T23:59:59",
  "customerName": "Nguyễn Văn A"
}
```

## 🚀 API Endpoints

### 1. Tạo lịch sử nạp tiền mới
```http
POST /api/recharge-history
Content-Type: application/json

{
  "customerId": 123,
  "amount": 100000.00
}
```

**Response:**
```json
{
  "rechargeId": 1,
  "customerId": 123,
  "amount": 100000.00,
  "rechargeDate": "2024-01-15T10:30:00",
  "customerName": "Nguyễn Văn A"
}
```

### 2. Lấy lịch sử nạp tiền theo ID
```http
GET /api/recharge-history/{rechargeId}
```

**Response:**
```json
{
  "rechargeId": 1,
  "customerId": 123,
  "amount": 100000.00,
  "rechargeDate": "2024-01-15T10:30:00",
  "customerName": "Nguyễn Văn A"
}
```

### 3. Lấy tất cả lịch sử nạp tiền (có phân trang)
```http
GET /api/recharge-history?page=0&size=10&sortBy=rechargeDate&sortDir=desc
```

**Query Parameters:**
- `page`: Số trang (mặc định: 0)
- `size`: Kích thước trang (mặc định: 10)
- `sortBy`: Trường sắp xếp (mặc định: rechargeDate)
- `sortDir`: Hướng sắp xếp (asc/desc, mặc định: desc)

**Response:**
```json
{
  "content": [
    {
      "rechargeId": 1,
      "customerId": 123,
      "amount": 100000.00,
      "rechargeDate": "2024-01-15T10:30:00",
      "customerName": "Nguyễn Văn A"
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10,
    "sort": {
      "sorted": true,
      "unsorted": false
    }
  },
  "totalElements": 1,
  "totalPages": 1,
  "first": true,
  "last": true,
  "numberOfElements": 1
}
```

### 4. Lấy lịch sử nạp tiền theo Customer ID
```http
GET /api/recharge-history/customer/{customerId}
```

**Response:**
```json
[
  {
    "rechargeId": 1,
    "customerId": 123,
    "amount": 100000.00,
    "rechargeDate": "2024-01-15T10:30:00",
    "customerName": "Nguyễn Văn A"
  }
]
```

### 5. Lấy lịch sử nạp tiền theo Customer ID (có phân trang)
```http
GET /api/recharge-history/customer/{customerId}/paged?page=0&size=10&sortBy=rechargeDate&sortDir=desc
```

**Response:** Tương tự endpoint 3

### 6. Tìm kiếm lịch sử nạp tiền với filters
```http
POST /api/recharge-history/search?page=0&size=10&sortBy=rechargeDate&sortDir=desc
Content-Type: application/json

{
  "customerId": 123,
  "startDate": "2024-01-01T00:00:00",
  "endDate": "2024-01-31T23:59:59",
  "customerName": "Nguyễn Văn A"
}
```

**Response:** Tương tự endpoint 3

### 7. Lấy tổng tiền nạp của customer
```http
GET /api/recharge-history/customer/{customerId}/total
```

**Response:**
```json
500000.00
```

### 8. Lấy tổng tiền nạp của customer trong khoảng thời gian
```http
GET /api/recharge-history/customer/{customerId}/total/date-range?startDate=2024-01-01&endDate=2024-01-31
```

**Query Parameters:**
- `startDate`: Ngày bắt đầu (yyyy-MM-dd)
- `endDate`: Ngày kết thúc (yyyy-MM-dd)

**Response:**
```json
300000.00
```

### 9. Xóa lịch sử nạp tiền
```http
DELETE /api/recharge-history/{rechargeId}
```

**Response:** 204 No Content

## 🔄 Tính năng tự động

### Auto-update Customer Balance
- Khi tạo lịch sử nạp tiền mới: `customer.balance += amount`
- Khi xóa lịch sử nạp tiền: `customer.balance -= amount`
- Đảm bảo tính toàn vẹn dữ liệu với transaction

### Auto-timestamp
- Thời gian nạp tiền được tự động lưu khi tạo mới
- Format: `yyyy-MM-ddTHH:mm:ss`

## 📝 Validation Rules

### CreateRechargeHistoryRequestDTO
- `customerId`: Bắt buộc, phải tồn tại trong database
- `amount`: Bắt buộc, phải > 0

### Search Filters
- `customerId`: Tìm theo ID khách hàng
- `startDate`: Tìm từ ngày này
- `endDate`: Tìm đến ngày này
- `customerName`: Tìm theo tên khách hàng (partial match, case-insensitive)

## 🚨 Error Responses

### 400 Bad Request
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Amount must be greater than 0",
  "path": "/api/recharge-history"
}
```

### 404 Not Found
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 404,
  "error": "Not Found",
  "message": "Customer not found with id: 123",
  "path": "/api/recharge-history"
}
```

### 500 Internal Server Error
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Database connection failed",
  "path": "/api/recharge-history"
}
```

## 💡 Sử dụng trong giao diện

### 1. Form tạo lịch sử nạp tiền
```javascript
const createRechargeHistory = async (customerId, amount) => {
  const response = await fetch('/api/recharge-history', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({
      customerId: customerId,
      amount: amount
    })
  });
  return response.json();
};
```

### 2. Lấy danh sách lịch sử nạp tiền
```javascript
const getRechargeHistory = async (page = 0, size = 10) => {
  const response = await fetch(`/api/recharge-history?page=${page}&size=${size}&sortBy=rechargeDate&sortDir=desc`);
  return response.json();
};
```

### 3. Tìm kiếm lịch sử nạp tiền
```javascript
const searchRechargeHistory = async (filters, page = 0, size = 10) => {
  const response = await fetch(`/api/recharge-history/search?page=${page}&size=${size}`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(filters)
  });
  return response.json();
};
```

### 4. Lấy tổng tiền nạp của customer
```javascript
const getTotalRechargeAmount = async (customerId) => {
  const response = await fetch(`/api/recharge-history/customer/${customerId}/total`);
  return response.json();
};
```

## 🔧 Cấu hình Swagger
API được tích hợp với Swagger UI tại: `http://localhost:8080/swagger-ui.html`

## 📊 Database Schema
```sql
CREATE TABLE recharge_history (
  recharge_id INT NOT NULL AUTO_INCREMENT,
  customer_id INT NOT NULL,
  amount DECIMAL(12,2) NOT NULL,
  recharge_date DATETIME DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (recharge_id),
  CONSTRAINT fk_recharge_customer FOREIGN KEY (customer_id)
    REFERENCES customer(customer_id)
    ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
```
