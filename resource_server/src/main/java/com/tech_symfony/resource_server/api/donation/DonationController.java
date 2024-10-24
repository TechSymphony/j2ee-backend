package com.tech_symfony.resource_server.api.donation;

import com.tech_symfony.resource_server.api.donation.viewmodel.DonationListVm;
import com.tech_symfony.resource_server.api.donation.viewmodel.DonationPostVm;
import com.tech_symfony.resource_server.api.user.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/donations")
public class DonationController {

    private final DonationService donationService;
    private final AuthService userAuthUtilService;

    @GetMapping
    public List<DonationListVm> getAllDonations() {
        return donationService.findAll();
    }



    @Operation(
            summary = "Thêm hóa đơn kèm vé trước khi thanh toán",
            description = "API thêm hóa đơn kèm vé trước khi thanh toán, hóa đơn khi vừa thêm sẽ có trạng thái mặc định " +
                    "là `Unpaid`, thời hạn thanh toán là 15 phút, nếu sau 15 phút không thực hiện thanh toán, hóa đơn và " +
                    "vé sẽ được xóa khỏi cơ sở dữ liệu."
    )
    @PostMapping(value = "/donations")
    @ResponseStatus(HttpStatus.CREATED)
    public Donation create(
            @Valid @RequestBody DonationPostVm dataRaw
    ) {

        return donationService.create(dataRaw, userAuthUtilService.getCurrentUserAuthenticated().getUsername());
//        Link link = entityLinks.linkToItemResource(Bill.class, newBill.getId());
//        return ResponseEntity
//                .created(link.toUri())
//                .body(billModelAssembler.toModel(newBill));
    }



    @Operation(
            summary = "Thực hiện thanh toán",
            description = "API được gọi khi vừa thanh toán và chuyển về trang thông báo, API sẽ trả về kết quả cần thiết " +
                    "để hiển thị với khách hàng, kết quả có thể thành công hoặc thất bại. Khi thất bại, lí do " +
                    "sẽ được nêu rõ. "
    )
    @PutMapping(value = "/donations/{id}/payment")
    public Donation pay(
            @PathVariable int id
    ) {
            return donationService.pay(id, userAuthUtilService.getCurrentUserAuthenticated().getUsername());
//        return billModelAssembler.toModel(billService.pay(id, userAuthUtilService.getCurrentUsername()));
    }

}
