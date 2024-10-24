package com.tech_symfony.resource_server.api.donation.vnpay;


import com.fasterxml.jackson.databind.util.JSONPObject;
import com.tech_symfony.resource_server.api.donation.Donation;
import com.tech_symfony.resource_server.api.donation.DonationController;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.json.JSONObject;
import org.springframework.web.reactive.function.client.WebClient;


import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
//import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequiredArgsConstructor
@Service
public class VnpayService {

    private final VnpayConfig vnpayConfig;

    public String doPost(Donation donation) {

        String orderType = "190001";
        BigDecimal amount = donation.getAmountTotal() // Lấy giá trị của amountTotal
                .multiply(new BigDecimal(100))            // Nhân với 100 bằng phương thức multiply
                .setScale(0, BigDecimal.ROUND_HALF_UP);
        String billId = donation.getId().toString();
        String vnp_TxnRef = billId;
        String vnp_IpAddr = "127.0.0.1";

        String vnp_TmnCode = vnpayConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnpayConfig.vnp_Version);
        vnp_Params.put("vnp_Command", vnpayConfig.vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        String vnp_CreateDate = formatter.format(bill.getCreateTime());
        String vnp_CreateDate = formatter.format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", "VND");
        //        if (bankCode != null && !bankCode.isEmpty()) {
//            vnp_Params.put("vnp_BankCode", bankCode);
//        }
        vnp_Params.put("vnp_BankCode", "NCB");

        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", "vn");

        vnp_Params.put("vnp_OrderInfo", "Thanh toan don hang:" + billId);
        vnp_Params.put("vnp_OrderType", orderType);

        vnp_Params.put("vnp_ReturnUrl", linkTo(methodOn(DonationController.class).pay(donation.getId())).toString());
//        vnp_Params.put("vnp_ReturnUrl", vnpayConfig.vnp_ReturnUrl);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(Date.from(donation.getDonationDate()));
        calendar.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(calendar.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);


        // Ma hoa chuoi thong tin
        List fieldNames = new ArrayList(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
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
        String paymentUrl = vnpayConfig.vnp_PayUrl + "?" + queryUrl;

        return paymentUrl;
    }

    public JSONObject verifyPay(Donation donationEntity) {
        String vnp_RequestId = vnpayConfig.getRandomNumber(8);
        String vnp_Command = "querydr";
        String vnp_TxnRef = donationEntity.getId().toString();
        String vnp_OrderInfo = "Kiem tra ket qua GD don hang " + vnp_TxnRef;

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());

        String vnp_TransDate = formatter.format(donationEntity.getDonationDate());

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


        String hash_Data = vnp_RequestId + "|" + vnpayConfig.vnp_Version + "|" + vnp_Command + "|" + vnpayConfig.vnp_TmnCode + "|" + vnp_TxnRef
                + "|" + vnp_TransDate + "|" + vnp_CreateDate + "|" + vnp_IpAddr + "|" + vnp_OrderInfo;

        String vnp_SecureHash = vnpayConfig.hmacSHA512(vnpayConfig.secretKey, hash_Data);

        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        String responseBody = webClient.post()
                .uri(vnpayConfig.vnp_ApiUrl)
                .bodyValue(vnp_Params)
                .exchange()
                .block().bodyToMono(String.class).block();

//		String responseBody = response.flatMap(res -> res.bodyToMono(String.class)).block();

        System.out.println(responseBody);

        JSONObject json = new JSONObject(responseBody);
        System.out.println(json);

        String res_ResponseCode = (String) json.get("vnp_ResponseCode");
//        String res_TxnRef = (String) json.get("vnp_TxnRef");
        String res_Message = (String) json.get("vnp_Message");
//        Double res_Amount = Double.valueOf((String) json.get("vnp_Amount")) / 100;
        String res_TransactionType = (String) json.get("vnp_TransactionType");
        String res_TransactionStatus = (String) json.get("vnp_TransactionStatus");

        System.out.println(res_Message);
        checkResponse(res_ResponseCode, res_TransactionType, res_TransactionStatus);

        return json;
    }

    public void checkResponse(String res_ResponseCode, String res_TransactionType, String res_TransactionStatus) {
        if (res_ResponseCode.equals("09")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Thẻ/Tài khoản của khách hàng chưa đăng ký dịch vụ InternetBanking tại ngân hàng"));

        if (res_ResponseCode.equals("10")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Khách hàng xác thực thông tin thẻ/tài khoản không đúng quá 3 lần"));

        if (res_ResponseCode.equals("11")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Đã hết hạn chờ thanh toán. Xin quý khách vui lòng thực hiện lại giao dịch."));

        if (res_ResponseCode.equals("12")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Thẻ/Tài khoản của khách hàng bị khóa."));

        if (res_ResponseCode.equals("24")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Khách hàng hủy giao dịch."));

        if (res_ResponseCode.equals("51")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Tài khoản của quý khách không đủ số dư để thực hiện giao dịch."));

        if (res_ResponseCode.equals("65")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Tài khoản của Quý khách đã vượt quá hạn mức giao dịch trong ngày."));

        if (res_ResponseCode.equals("75")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Ngân hàng thanh toán đang bảo trì.."));

        if (res_ResponseCode.equals("79")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("KH nhập sai mật khẩu thanh toán quá số lần quy định. Xin quý khách vui lòng thực hiện lại giao dịch"));

        if (res_ResponseCode.equals("99")) // Response Code invaild
            throw new TransactionException("Transaction failed", 402, List.of("Lỗi không xác định."));

        if (!res_TransactionType.equals("01")) // Transaction Type invaild
            throw new TransactionException("Transaction failed", 402, List.of("Transaction Type invaild"));

        if (res_TransactionStatus.equals("01")) // Transaction is pending
            throw new TransactionException("Transaction failed", 402, List.of("Chưa thanh toán"));

        if (!res_TransactionStatus.equals("00")) // Transaction Status invaild
            throw new TransactionException("Transaction failed", 402, List.of("Transaction Status invaild"));
    }

}

