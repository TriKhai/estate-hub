# Mẫu JDBC: tìm tòa nhà theo ID

Mục tiêu: gọi `GET /api/building/1` và lấy một tòa nhà từ MySQL.
Project đang dùng Spring Boot cho HTTP; phần truy vấn bên dưới là JDBC trực tiếp, không dùng JPA.

## Đọc code theo thứ tự

1. `api/BuildingAPI.java`: nhận ID từ đường dẫn. ID không dương trả 400; không tìm thấy trả 404; tìm thấy trả 200 với JSON.
2. `service/impl/BuildingServiceImpl.java`: gọi repository, chuyển entity thành DTO và ghép địa chỉ. Không có SQL ở đây.
3. `repository/impl/BuildingRepositoryImpl.java`: hàm `findById` mở kết nối, chạy SQL và đọc kết quả.

## Sáu bước JDBC trong findById

```java
String sql = "SELECT name, street, ward, numberofbasement FROM building WHERE id = ?";
```

Chọn đúng các cột cần đọc. `?` là chỗ đặt giá trị ID.

```java
Connection connection = DriverManager.getConnection(DB_URL, USER, PASS);
PreparedStatement statement = connection.prepareStatement(sql);
statement.setLong(1, id);
ResultSet resultSet = statement.executeQuery();
```

1. Connection là kết nối đến MySQL.
2. PreparedStatement là câu lệnh SQL có tham số.
3. setLong gán ID vào dấu hỏi thứ nhất. Thứ tự tham số bắt đầu từ 1.
4. executeQuery chạy SELECT và trả ResultSet.
5. ResultSet ban đầu đứng trước dòng đầu tiên. next() di chuyển đến dòng tiếp theo; false nghĩa là không có dòng.
6. getString/getInt đọc từng cột rồi setter đưa dữ liệu vào BuildingEntity.

Trong source thật, các tài nguyên nằm trong try-with-resources để được đóng kể cả khi có lỗi. ResultSet đóng trước statement, rồi connection.

`getInt` trả 0 khi cột SQL là NULL. Vì vậy phải gọi `wasNull()` ngay sau khi đọc để giữ đúng giá trị null của Integer.

Không có dòng thì trả null theo quy ước của mẫu này. SQLException thì ném exception lên trên; không biến lỗi kết nối thành trường hợp không tìm thấy.

## Thử bằng Postman

Chạy Application bằng IDE, bảo đảm MySQL hoạt động và database `estatebasic` có bảng `building`. Dùng cổng ứng dụng thực tế; ví dụ dưới đây giả sử cổng 8080.

- `GET http://localhost:8080/api/building/1`: 200 nếu bản ghi 1 tồn tại.
- Dùng một ID chắc chắn không có trong database: 404.
- `GET http://localhost:8080/api/building/0`: 400.
- `GET http://localhost:8080/api/building/abc`: 400 do Spring không chuyển được abc sang Long.

Ví dụ cấu trúc JSON trả về (giá trị tùy dữ liệu của bạn):

```json
{
  "name": "Tòa nhà A",
  "numberOfBasement": 2,
  "address": "123 Nguyễn Văn A, Phường B"
}
```

## Lưu ý về project hiện tại

- Hàm findAll cũ vẫn nối dữ liệu người dùng vào SQL. Bài tiếp theo nên chuyển hàm này sang PreparedStatement.
- Cấu hình kết nối hiện được đặt trong source mẫu. Khi chuẩn bị chia sẻ hoặc triển khai, chuyển thông tin đăng nhập sang cấu hình bên ngoài.
- Mẫu này mở một connection mỗi lần gọi để dễ hiểu. Khi phát triển tiếp, học DataSource và connection pool.

## Bài bạn tự làm tiếp

Thêm cột rentprice vào SELECT, thêm trường rentPrice vào entity và DTO, đọc bằng ResultSet, rồi trả giá thuê trong JSON. Làm đủ cả ba lớp để hiểu dữ liệu đi từ database đến API.
