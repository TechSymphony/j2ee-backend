# Xây dựng Website từ thiện cho cơ sở giáo dục đại học

# Tổng quan đề tài

## 1.1	Đặt vấn đề
Trong môi trường giáo dục, các hoạt động từ thiện không chỉ thể hiện tinh thần tương thân tương ái mà còn là một trong những tiêu chí đánh giá điểm rèn luyện của sinh viên. Tuy nhiên, thực tế hiện nay cho thấy việc quản lý và tổ chức các chiến dịch từ thiện còn gặp nhiều khó khăn:
- Thiếu minh bạch: Nhiều chiến dịch từ thiện không được công khai đầy đủ thông tin, gây mất niềm tin từ người đóng góp.
-	Quy trình thủ công: Hầu hết các khâu như đăng ký, xác minh, kiểm duyệt nội dung, báo cáo tài chính và kiểm tra danh sách sinh viên tham gia đều được thực hiện thủ công, dễ dẫn đến sai sót và mất thời gian.
-	Khó quản lý dữ liệu: Không có hệ thống quản lý tập trung, dẫn đến khó khăn trong việc theo dõi các chiến dịch, người thụ hưởng, hay các đóng góp từ người tài trợ.

Nhằm giải quyết các vấn đề trên, nhóm đề xuất xây dựng một hệ thống website quản lý chiến dịch từ thiện. Hệ thống này sẽ hỗ trợ tự động hóa quy trình, đảm bảo tính công khai, minh bạch và nâng cao hiệu quả quản lý. Qua đó, không chỉ cải thiện trải nghiệm của người tham gia mà còn góp phần nâng cao uy tín và sự chuyên nghiệp của tổ chức.
## 1.2	Nghiệp vụ
## 1.2.1	Quy trình phê duyệt và kiểm duyệt nội dung từ thiện
Bước 1: Người thụ hưởng gửi yêu cầu nguyện vọng quyên góp, cung cấp các thông tin như mục tiêu quyên góp, hoàn cảnh khó khăn.
Bước 2:  Phòng CTSV sẽ xác minh số lần đã nhận hỗ trợ, thông tin người thụ hưởng và tình trạng khó khăn để xác thực độ uy tín của người gửi nguyện vọng trước khi chấp nhận yêu cầu, kèm theo các điều kiện ràng buộc về pháp luật (nếu có).
Bước 3: Bộ phận Công tác Sinh viên (CTSV) sau khi kiểm duyệt sẽ thực hiện chuyển đổi nguyện vọng trên thành chiến dịch.
Bước 4: phòng CTSV xác nhận lại thông tin chiến dịch và đăng tải chiến dịch công khai.
## 1.2.2	Quy trình quyên góp, báo cáo và minh bạch tài chính
Bước 1: Người quyên góp tìm kiếm thông tin chiến dịch trên hệ thống.
Bước 2: Sau khi xem xét thông tin chiến dịch quyên góp, nhà hảo tâm sẽ thực hiện quyên góp thông qua hệ thống thanh toán tích hợp như Momo, VNPay,...
Bước 3: Phòng công tác sinh viên cập nhập tình hình quyên góp của chiến dịch thủ công hoặc tự động.
Bước 4: Xử lý chiến dịch quyên góp khi đạt mục tiêu hoặc quá hạn.
Bước 4.1: Khi chiến dịch vượt quá thời hạn mà không đạt mục tiêu, phòng công tác sinh viên sẽ gia hạn thêm thời gian cho tới khi đạt đủ số tiền yêu cầu hoặc là sẽ dùng số tiền đó cho 1 chiến dịch khác.
Bước 4.2: Khi chiến dịch đã đạt mục tiêu thì ngưng nhận quyên góp, phần tiền được quyên góp sẽ được Cán bộ CTSV liên hệ tới người thụ hưởng và gửi cho họ.
Bước 5: Cán bộ CTSV có thể xem thống kê số tiền quyên góp của các chiến dịch, họ cũng có thể xuất danh sách quyên góp tùy vào nhu cầu kê khai, sao kê.
Bước 6: Phòng CTSV cập nhập tình hình chi tiêu với số tiền quyên góp của 1 chiến dịch để tạo niềm tin.
## 1.2.3	Quy trình quản lý và theo dõi điểm rèn luyện cho sinh viên
Bước 1: Sinh viên có thể theo dõi lịch sử tham gia các hoạt động từ thiện để điền vào phiếu điểm rèn luyện phù hợp.
Bước 2: Phòng CTSV xuất các khoản đóng góp của các sinh viên.
Bước 3: Phòng CTSV sẽ xem xét và cập nhật điểm rèn luyện dựa trên sự tham gia của sinh viên. 
## 1.3	Đối tượng người dùng

### Bảng: Actor và Nhu cầu/Những mối quan tâm

| **STT** | **Actor**                   | **Nhu cầu/Những mối quan tâm**                                                                                      |
|---------|-----------------------------|--------------------------------------------------------------------------------------------------------------------|
| 1       | **Phòng công tác sinh viên** | - Là bộ phận tham gia vào quá trình khởi tạo các chiến dịch quyên góp.                                             |
|         |                             | - Xem xét thông tin và xét duyệt các nguyện vọng được gửi bởi người quyên góp.                                     |
|         |                             | - Chuyển đổi nguyện vọng đã được xét duyệt thành chiến dịch tương ứng.                                             |
|         |                             | - Xử lý chiến dịch quyên góp khi đạt mục tiêu hoặc quá hạn.                                                        |
|         |                             | - Nhập dữ liệu danh sách sinh viên vào hệ thống để sinh viên có thể trở thành người quyên góp và dễ dàng tra cứu thông tin quyên góp. |
|         |                             | - Xuất danh sách các khoản đóng góp của sinh viên.                                                                 |
|         |                             | - Căn cứ theo danh sách quyên góp của sinh viên để làm tiêu chí xét điểm rèn luyện.                                |
|         |                             | - Cập nhật thông tin về tình hình quyên góp của chiến dịch sau khi thực hiện quy trình trao tiền đến người thụ hưởng. |
| 2       | **Người quyên góp**          | - Tra cứu và xem xét thông tin chiến dịch quyên góp.                                                              |
|         |                             | - Quản lý hồ sơ cá nhân trên hệ thống.                                                                             |
|         |                             | - Quyên góp cho các chiến dịch trên hệ thống.                                                                      |
|         |                             | - Xem xét thông tin chi tiết của chiến dịch để cân nhắc tham gia quyên góp.                                        |
|         |                             | - Xem xét thông tin đóng góp của bản thân hoặc các cá nhân khác ở các chiến dịch.                                  |
|         |                             | - Đối với sinh viên, minh chứng quyên góp sẽ được cộng vào điểm rèn luyện theo quy định của phòng công tác sinh viên. |
|         |                             | - Xem thông tin cập nhật tình hình chiến dịch sau quyên góp để làm cơ sở cho các lần đóng góp tiếp theo.           |
| 3       | **Người thụ hưởng**          | - Chủ động gửi nguyện vọng lên hệ thống để nhận quyên góp hoặc liên hệ trực tiếp với phòng công tác sinh viên.     |
|         |                             | - Sau khi chiến dịch hoàn tất quyên góp, phòng công tác sinh viên sẽ liên hệ để thực hiện các thủ tục tiếp theo.    |


## 1.4	Usecase tổng quát
![image](https://github.com/user-attachments/assets/dcf4a7eb-b698-4ddb-9ce7-2d4d6ec2631e)

# Kiến trúc chương trình và các công nghệ sử dụng
![image](https://github.com/user-attachments/assets/3aa5fd67-7c65-4a7b-8bf6-88aa2409fad3)

# Database
## Concept
![image](https://github.com/user-attachments/assets/f6e1f8eb-6505-459b-be18-3729fc8c5e42)

## Logic
![web tu thien khoi dau](https://github.com/user-attachments/assets/2d84612b-25a5-4bcd-baf7-6b430d98ea89)

# Demo

## Xem chiến dịch
![image](https://github.com/user-attachments/assets/a9a3c900-89c9-45e8-aaf9-c775d79c0ad3)
![image](https://github.com/user-attachments/assets/62ef2268-0159-4a28-867b-e7e18b9217b0)

## Quyên góp
![image](https://github.com/user-attachments/assets/2621341d-3745-459a-9c43-76fbba46f457)
![image](https://github.com/user-attachments/assets/02923fd3-f68d-4ca6-b905-8186231b4355)

## Thống kê 
![image](https://github.com/user-attachments/assets/1f581b45-efd7-4fe9-894d-72c37ca1e339)

## Duyệt nguyện vọng
![image](https://github.com/user-attachments/assets/b18641e4-58ba-4107-b6a3-dade43076d1b)
![image](https://github.com/user-attachments/assets/909aed00-086e-405a-8ea3-192f79b1f72b)

## Quản lý chiến dịch
![image](https://github.com/user-attachments/assets/945ca0fe-e078-4f22-97f9-063f47d2eb42)

## Đăng nhập 
![image](https://github.com/user-attachments/assets/551fd608-9768-464e-ba72-bb67be3603c0)

# Usage

1. Clone the repository
2. Go to auth-server directory and run the following command

```bash
./mvnw spring-boot:run
```

3. open inteliji and open the project in resource-server directory

```bash
./mvnw spring-boot:run
```

