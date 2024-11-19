package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationPostVm;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.xml.bind.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/public/donations")
@Controller
public class DonationClientController {

    private final DonationService donationService;

    @Operation(
            summary = "Thêm số tiền muốn từ thiện trước khi thanh toán",
            description = "API này cho phép thêm hóa đơn kèm theo vé trước khi thanh toán. " +
                    "Khi vừa tạo, hóa đơn sẽ có trạng thái mặc định là `IN_PROGRESS` với thời hạn thanh toán là 15 phút. " +
                    "Nếu không hoàn tất thanh toán trong khoảng thời gian này, hóa đơn được update là HOLDING để chờ xem có lỗi gì không."
    )
    @PostMapping()
    @ResponseBody
    @ResponseStatus(HttpStatus.CREATED)
    public Donation create(
            @Valid @RequestBody DonationPostVm dataRaw
    ) throws ValidationException {

        return donationService.create(dataRaw);
    }

    @Operation(
            summary = "Thực hiện thanh toán",
            description = "API được frontend gọi nếu thanh toán thành công và giới hạn chỉ 1 lần gọi. " +
                    "Khi gọi API này, hóa đơn sẽ được update trạng thái thành `PAID` và thời gian thanh toán."
    )
    @PutMapping(value = "/{id}/payment/event")
    @ResponseBody
    public void sendVerifyEvent(
            @PathVariable Integer id
    ) {
        donationService.sendEventVerifyClient(id);
    }

}
