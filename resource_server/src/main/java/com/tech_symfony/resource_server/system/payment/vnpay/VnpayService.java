package com.tech_symfony.resource_server.system.payment.vnpay;


import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.system.config.JacksonConfig;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Service
public class VnpayService implements PaymentService<Donation, JSONObject> {

    private final VnpayConfig vnpayConfig;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
            .withZone(TimeZone.getTimeZone(JacksonConfig.MY_TIME_ZONE).toZoneId());
    @Override
    public String createBill(Donation paymentEntity) {

        String orderType = "190001";
        BigDecimal amount = paymentEntity.getAmountTotal()
                .multiply(new BigDecimal(100))            // Nhân với 100 bằng phương thức multiply
                .setScale(0, RoundingMode.HALF_UP);

        String billId = paymentEntity.getId().toString();

        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = vnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", vnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));

        String vnp_CreateDate = formatter.format(paymentEntity.getDonationDate());

        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", "VND");

//        vnp_Params.put("vnp_BankCode", "NCB");

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_OrderInfo", paymentEntity.getMessage());
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.vnp_ReturnFrontendUrl);

        // Get the donation date as an Instant
        Instant donationInstant = paymentEntity.getDonationDate();

        Instant expireInstant = donationInstant.plus(vnpayConfig.exprationTime, ChronoUnit.MILLIS);


        String vnp_ExpireDate = formatter.format(expireInstant);
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        vnp_Params.put("vnp_TxnRef", billId);


        // Ma hoa chuoi thong tin
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (!fieldValue.isEmpty())) {
                //Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                //Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = vnpayConfig.hmacSHA512(vnpayConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;

        // payment Url
        return vnpayConfig.vnp_PayUrl + "?" + queryUrl;
    }

    @Override
    public JSONObject verifyPay(Donation paymentEntity) {
        String vnp_RequestId = vnpayConfig.getRandomNumber(8);
        String vnp_Command = "querydr";
        String vnp_TxnRef = paymentEntity.getId().toString();
        String vnp_OrderInfo = "Kiem tra ket qua GD don hang:" + vnp_TxnRef;

        String vnp_CreateDate = formatter.format(paymentEntity.getCreateTime());

        String vnp_TransDate = formatter.format(paymentEntity.getDonationDate());

        String vnp_IpAddr = "127.0.0.1";

        WebClient webClient = WebClient.create();

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_RequestId", vnp_RequestId);
        vnp_Params.put("vnp_Version", vnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnpayConfig.vnp_TmnCode);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_TransactionDate", vnp_TransDate);

        // vnp_Params.put("vnp_TransactionNo", vnp_TransactionNo);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        String hash_Data = vnp_RequestId + "|"
                + vnpayConfig.vnp_Version + "|"
                + vnp_Command + "|"
                + vnpayConfig.vnp_TmnCode + "|"
                + vnp_TxnRef + "|"
                + vnp_TransDate + "|"
                + vnp_CreateDate + "|"
                + vnp_IpAddr + "|"
                + vnp_OrderInfo;

        String vnp_SecureHash = vnpayConfig.hmacSHA512(vnpayConfig.secretKey, hash_Data);

        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        String responseBody = webClient.post()
                .uri(vnpayConfig.vnp_ApiUrl)
                .bodyValue(vnp_Params)
                .retrieve()
                .bodyToMono(String.class)
                .block();

//		String responseBody = response.flatMap(res -> res.bodyToMono(String.class)).block();

        System.out.println(responseBody);

        JSONObject json = new JSONObject(responseBody);
        System.out.println(json);

        String res_ResponseCode = (String) json.get("vnp_ResponseCode");
//        String res_TxnRef = (String) json.get("vnp_TxnRef");
        String res_Message = json.optString("vnp_Message", "");
//        Double res_Amount = Double.valueOf((String) json.get("vnp_Amount")) / 100;
        String res_TransactionStatus = json.optString("vnp_TransactionStatus", "");
        String res_TransactionType = json.optString("vnp_TransactionType", "");
        checkResponse(res_ResponseCode, res_TransactionType, res_TransactionStatus);

        return json;
    }

    private void checkResponse(String res_ResponseCode, String res_TransactionType, String res_TransactionStatus) {
        switch (res_ResponseCode) {
            case "09" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng"));
            case "10" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần"));
            case "11" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch."));
            case "12" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Thẻ/Tài khoản của khách hàng bị khóa."));
            case "24" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Khách hàng hủy giao dịch."));
            case "51" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Tài khoản của quý khách không đủ số dư để thực hiện giao dịch."));
            case "65" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày."));
            case "75" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Ngân hàng thanh toán đang bảo trì.."));
            case "79" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch"));
            case "99" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Lỗi không xác định."));
            case "94" ->
            // Response Code invaild
                    throw new TransactionException("Transaction failed", 402, List.of("Yêu cầu trùng lặp, duplicate request trong thời gian giới hạn của API."));
        }

        if (!res_TransactionType.equals("01")) // Transaction Type invaild
            throw new TransactionException("Transaction failed", 402, List.of("Transaction Type invaild"));

        if (res_TransactionStatus.equals("01")) // Transaction is pending
            throw new TransactionException("Transaction failed", 402, List.of("Chưa thanh toán"));

        if (!res_TransactionStatus.equals("00")) // Transaction Status invaild
            throw new TransactionException("Transaction failed", 402, List.of("Transaction Status invaild"));
    }

}

