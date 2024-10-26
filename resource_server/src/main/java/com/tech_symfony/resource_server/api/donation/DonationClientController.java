package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationPostVm;
import com.tech_symfony.resource_server.system.payment.vnpay.VnpayConfig;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@RequiredArgsConstructor
@RequestMapping("/public/donations")
@Controller
public class DonationClientController {

    private final DonationService donationService;
    private final VnpayConfig vnpayConfig;

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
    ) {

        return donationService.create(dataRaw);
    }

    @Operation(
            summary = "Thực hiện thanh toán",
            description = "API được gọi khi vừa thanh toán và chuyển về trang thông báo, API sẽ trả về kết quả cần thiết " +
                    "để hiển thị với khách hàng, kết quả có thể thành công hoặc thất bại. Khi thất bại, lí do " +
                    "sẽ được nêu rõ. "
    )
    @GetMapping(value = "/{id}/payment")
    public ModelAndView pay(
            @PathVariable int id
    ) {
        donationService.pay(id);

        return new ModelAndView("redirect:"+vnpayConfig.vnp_ReturnFrontendUrl);
    }

}
