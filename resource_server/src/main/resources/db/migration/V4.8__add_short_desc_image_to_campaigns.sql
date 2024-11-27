-- Add short_description column
ALTER TABLE campaigns
    ADD COLUMN short_description TEXT;

-- Add image column as an array of text (for storing multiple image URLs)
ALTER TABLE campaigns
    ADD COLUMN image TEXT;

ALTER TABLE campaigns
    ALTER COLUMN target_amount SET DATA TYPE DECIMAL(20, 2),
    ALTER COLUMN current_amount SET DATA TYPE DECIMAL(20, 2);


-- Update campaigns with short description and image

UPDATE campaigns
SET short_description = 'Hỗ trợ tài chính cho sinh viên du học',
    name = 'Hỗ trợ tài chính cho sinh viên du học',
    image = 'AgACAgUAAyEGAASG6F88AAMiZ0bghLia_2qYSAABqaANxxw0AAHdFAACe78xG5AYOFaZLGAIWDyPvgEAAwIAA3cAAzYE',
    description = '<p>Chiến dịch Hỗ trợ tài chính cho sinh viên du học ra đời với mục tiêu tiếp sức cho các bạn trẻ Việt Nam thực hiện ước mơ học tập tại các trường đại học danh tiếng trên thế giới. Nhiều sinh viên tài năng, giàu khát vọng nhưng đang gặp khó khăn về tài chính cần sự giúp đỡ để vượt qua rào cản này và tận dụng cơ hội vươn xa trên con đường học vấn.<br><br>Thông qua các khoản hỗ trợ học phí, chi phí sinh hoạt, hoặc tài trợ vé máy bay, chiến dịch mong muốn trở thành cầu nối để các bạn trẻ tiếp cận môi trường học tập quốc tế, mở rộng kiến thức và phát triển toàn diện. Chúng tôi kêu gọi sự chung tay từ cộng đồng, các doanh nghiệp và nhà hảo tâm để đồng hành cùng các tài năng trẻ trên hành trình mang tri thức về phục vụ quê hương, góp phần xây dựng một Việt Nam hội nhập và phát triển bền vững.</p>',
    target_amount = 100000000,
    current_amount = 22500000,
    disabled_at = TRUE
WHERE code = 'CAMP001';

UPDATE campaigns
SET short_description = 'Hỗ trợ sinh viên tham gia các hoạt động tình nguyện cộng đồng',
    name = 'Hỗ trợ sinh viên tham gia hoạt động tình nguyện',
    image = 'AgACAgUAAyEGAASG6F88AAMqZ0bh3DFaxUqN_P81X8PXZHuEK3QAAoW_MRuQGDhW3z46msFaNH4BAAMCAAN5AAM2BA',
    description = '<p>Chiến dịch Hỗ trợ sinh viên tham gia các hoạt động tình nguyện cộng đồng nhằm khuyến khích và tạo điều kiện cho các bạn sinh viên tham gia vào những hoạt động thiện nguyện, góp phần cải thiện chất lượng sống cho cộng đồng và phát triển kỹ năng cá nhân. Những hoạt động này có thể bao gồm hỗ trợ người nghèo, tổ chức các chương trình giáo dục, bảo vệ môi trường, giúp đỡ người cao tuổi hoặc những người khuyết tật.<br><br>Tuy nhiên, việc tham gia vào các hoạt động tình nguyện đôi khi yêu cầu chi phí cho việc di chuyển, tổ chức sự kiện hoặc mua sắm các vật dụng cần thiết. Chúng tôi kêu gọi sự đóng góp từ cộng đồng, các doanh nghiệp và tổ chức để cung cấp tài chính hỗ trợ sinh viên tham gia các chương trình này. Mỗi đóng góp sẽ giúp các sinh viên có cơ hội trải nghiệm thực tế, phát triển bản thân và đóng góp vào sự thay đổi tích cực trong xã hội. Bằng những hành động nhỏ, chúng ta có thể tạo ra sự khác biệt lớn cho cộng đồng.</p>',
    target_amount = 50000000,
    current_amount = 50000000
WHERE code = 'CAMP002';

UPDATE campaigns
    SET short_description = 'Quyên góp hàng tháng hỗ trợ sinh viên có hoàn cảnh khó khăn',
        image = 'AgACAgUAAyEGAASG6F88AAMeZ0bgHnlI7V2frfGK5MZVB7nqiHgAAnO_MRuQGDhWLsN8JaStGtIBAAMCAAN5AAM2BA',
        description = '<p>Chiến dịch <strong>Hỗ trợ Sinh viên Thiếu thốn</strong> được khởi xướng nhằm giúp đỡ các sinh viên có hoàn cảnh khó khăn vượt qua thách thức tài chính trong học tập. Chúng tôi mong muốn cung cấp các nguồn lực như <strong>học bổng</strong>, hỗ trợ vật chất và các chương trình cố vấn nhằm giúp các bạn sinh viên phát triển toàn diện.</p>,
                         <p>Với mục tiêu quyên góp trên, chiến dịch kêu gọi sự chung tay từ cộng đồng, các tổ chức và cá nhân hảo tâm. Hãy cùng nhau lan tỏa thông điệp yêu thương và trao cơ hội học tập cho những tài năng trẻ.</p>',
        target_amount = 10000000,
        current_amount = 2500000
    WHERE code = 'CHIEN_DICH_001';

UPDATE campaigns
    SET short_description = 'Quyên góp mua sắm thiết bị học tập cho sinh viên',
        image = 'AgACAgUAAyEGAASG6F88AAMfZ0bgOkgwH8vI8DZyP54fIJL8mhUAAne_MRuQGDhWybL3uEFVezgBAAMCAAN5AAM2BA',
        description = '<p>Chiến dịch <Strong>Quyên góp mua sắm thiết bị học tập cho học sinh, sinh viên</strong> nhằm hỗ trợ các em có hoàn cảnh khó khăn trên con đường chinh phục tri thức. Với mục tiêu mang đến những chiếc laptop, máy tính bảng, hoặc các dụng cụ học tập thiết yếu, chiến dịch hướng đến việc xóa bỏ rào cản về thiết bị, giúp các em tiếp cận kiến thức và phát triển kỹ năng trong thời đại số hóa. Chúng tôi kêu gọi sự chung tay của cộng đồng để mang lại cơ hội học tập công bằng, góp phần xây dựng một thế hệ trẻ tự tin, sáng tạo và sẵn sàng chinh phục tương lai. Mỗi đóng góp, dù lớn hay nhỏ, đều là một hành động thiết thực giúp các em có thêm động lực và điều kiện để học tập tốt hơn</p>',
        target_amount = 5000000
    WHERE code = 'CHIEN_DICH_002';

UPDATE campaigns
    SET short_description = 'Hỗ trợ sinh viên mới trong việc mua sắm sách vở, tài liệu học tập',
        image = 'AgACAgUAAyEGAASG6F88AAMgZ0bgUb0u036G240q65QuBAHxhWIAAni_MRuQGDhWc65qzV6b4PEBAAMCAAN4AAM2BA',
        description = '<p>Chiến dịch <strong>Hỗ trợ sinh viên mới trong việc mua sắm sách vở, tài liệu học tập</strong> được tổ chức nhằm đồng hành cùng các bạn tân sinh viên trên hành trình bước vào môi trường học tập mới. Với mong muốn giảm bớt gánh nặng tài chính, chiến dịch tập trung kêu gọi sự hỗ trợ từ cộng đồng để cung cấp sách giáo khoa, tài liệu chuyên ngành, và các dụng cụ học tập cần thiết. Đây không chỉ là sự hỗ trợ vật chất mà còn là nguồn động viên tinh thần to lớn, giúp các bạn sinh viên có khởi đầu thuận lợi hơn trong hành trình chinh phục tri thức. Hãy cùng chung tay, bởi một cuốn sách bạn tặng có thể mở ra cánh cửa tri thức cho một thế hệ trẻ đầy triển vọng.</p>',
        target_amount = 5000000,
        current_amount = 2000000
    WHERE code = 'CHIEN_DICH_003';

UPDATE campaigns
    SET short_description = 'Quyên góp học bổng cho sinh viên nghèo vượt khó học tập',
        image = 'AgACAgUAAyEGAASG6F88AAMhZ0bga67bIqdR6_nSd5hDKUBEzdcAAnm_MRuQGDhWxJLGzOdaVmgBAAMCAAN4AAM2BA',
        description = '<p>Chiến dịch Quyên góp học bổng cho sinh viên nghèo vượt khó học tập hướng đến việc tạo cơ hội cho những sinh viên có hoàn cảnh khó khăn nhưng giàu nghị lực vươn lên trong học tập. Học bổng không chỉ là sự hỗ trợ về mặt tài chính mà còn là lời khích lệ để các bạn trẻ tiếp tục nỗ lực, vượt qua thách thức và chinh phục ước mơ. Chúng tôi kêu gọi sự chung tay từ cộng đồng, doanh nghiệp và các nhà hảo tâm để trao những suất học bổng ý nghĩa, giúp các bạn sinh viên giảm bớt áp lực kinh tế và tập trung hoàn thiện tri thức, kỹ năng. Mỗi đóng góp của bạn không chỉ là món quà hôm nay, mà còn là sự đầu tư cho tương lai, ươm mầm những tài năng có thể tạo nên sự khác biệt cho xã hội sau này.</p>',
        target_amount = 10000000,
        current_amount = 10000000
    WHERE code = 'CHIEN_DICH_004';

UPDATE campaigns
    SET short_description = 'Hỗ trợ tài chính cho sinh viên du học',
        image = 'AgACAgUAAyEGAASG6F88AAMiZ0bghLia_2qYSAABqaANxxw0AAHdFAACe78xG5AYOFaZLGAIWDyPvgEAAwIAA3cAAzYE',
        description = '<p>Chiến dịch Hỗ trợ tài chính cho sinh viên du học ra đời với mục tiêu tiếp sức cho các bạn trẻ Việt Nam thực hiện ước mơ học tập tại các trường đại học danh tiếng trên thế giới. Nhiều sinh viên tài năng, giàu khát vọng nhưng đang gặp khó khăn về tài chính cần sự giúp đỡ để vượt qua rào cản này và tận dụng cơ hội vươn xa trên con đường học vấn.<br><br>Thông qua các khoản hỗ trợ học phí, chi phí sinh hoạt, hoặc tài trợ vé máy bay, chiến dịch mong muốn trở thành cầu nối để các bạn trẻ tiếp cận môi trường học tập quốc tế, mở rộng kiến thức và phát triển toàn diện. Chúng tôi kêu gọi sự chung tay từ cộng đồng, các doanh nghiệp và nhà hảo tâm để đồng hành cùng các tài năng trẻ trên hành trình mang tri thức về phục vụ quê hương, góp phần xây dựng một Việt Nam hội nhập và phát triển bền vững.</p>',
        target_amount = 10000000,
        current_amount = 2000000
    WHERE code = 'CHIEN_DICH_005';

UPDATE campaigns
    SET short_description = 'Quyên góp cho các dự án sáng tạo và nghiên cứu khoa học của sinh viên',
        image = 'AgACAgUAAyEGAASG6F88AAMkZ0bg3-9pqhykQrdH3j7sLegoOZcAAn6_MRuQGDhWpuz4CE5xjKQBAAMCAANtAAM2BA',
        description = '<p>Chiến dịch Quyên góp cho các dự án sáng tạo và nghiên cứu khoa học của sinh viên nhằm khuyến khích và hỗ trợ thế hệ trẻ thực hiện những ý tưởng sáng tạo, góp phần giải quyết các vấn đề thực tiễn trong đời sống và xã hội. Nhiều sinh viên tài năng với những dự án tiềm năng đang đối mặt với thách thức lớn về nguồn kinh phí để triển khai và hiện thực hóa ý tưởng của mình.<br><br>Thông qua chiến dịch, chúng tôi kêu gọi cộng đồng, doanh nghiệp và các tổ chức giáo dục cùng chung tay đầu tư vào tri thức trẻ, tài trợ thiết bị, nguyên liệu, và kinh phí để sinh viên có thể tập trung phát triển dự án. Mỗi sự đóng góp là một bước tiến giúp các bạn trẻ hiện thực hóa giấc mơ, mở ra các giải pháp sáng tạo, đồng thời xây dựng một thế hệ tri thức tiên phong, cống hiến cho sự phát triển bền vững của xã hội.</p>',
        target_amount = 15000000,
        current_amount = 5560000
    WHERE code = 'CHIEN_DICH_006';

UPDATE campaigns
    SET short_description = 'Quyên góp hỗ trợ sinh viên đi thực tập ở nước ngoài',
        image = 'AgACAgUAAyEGAASG6F88AAMlZ0bg-jQqoUx0-M4MlTP_VwK_yQcAAn-_MRuQGDhW8T7Ffu6EY-sBAAMCAAN5AAM2BA',
        description = '<p>Chiến dịch Quyên góp hỗ trợ sinh viên đi thực tập ở nước ngoài nhằm tạo cơ hội cho các bạn sinh viên trải nghiệm môi trường làm việc quốc tế, nâng cao kỹ năng và mở rộng cơ hội nghề nghiệp. Việc thực tập tại các công ty, tổ chức uy tín ở nước ngoài không chỉ giúp sinh viên học hỏi từ các chuyên gia mà còn mang lại những kiến thức, kỹ năng thực tế, đồng thời giúp các bạn phát triển mạng lưới quan hệ toàn cầu.<br><br>Tuy nhiên, chi phí cho chương trình thực tập quốc tế thường là một rào cản lớn đối với nhiều sinh viên. Vì vậy, chiến dịch kêu gọi sự hỗ trợ từ cộng đồng, doanh nghiệp và các tổ chức để cung cấp học bổng, tài trợ chi phí sinh hoạt, vé máy bay và các khoản chi phí liên quan. Mỗi đóng góp của bạn sẽ giúp sinh viên có cơ hội học hỏi và phát triển, đồng thời đóng góp vào việc xây dựng một thế hệ chuyên gia trẻ với khả năng hội nhập và cống hiến cho xã hội.</p>',
        target_amount = 20000000,
        current_amount = 8856000
    WHERE code = 'CHIEN_DICH_007';

UPDATE campaigns
    SET short_description = 'Quyên góp sách vở, tài liệu học tập miễn phí cho sinh viên',
        image = 'AgACAgUAAyEGAASG6F88AAMmZ0bhLDdDEH9IP9vFx0-rs1izIKAAAoC_MRuQGDhWGByqfJ9so5cBAAMCAAN4AAM2BA',
        description = '<p>Chiến dịch Quyên góp sách vở, tài liệu học tập miễn phí cho sinh viên nhằm hỗ trợ các bạn sinh viên có hoàn cảnh khó khăn, giúp họ tiếp cận nguồn tài liệu học tập đầy đủ và chất lượng mà không phải lo lắng về chi phí. Sách vở, tài liệu học tập là những công cụ thiết yếu trong quá trình học tập, nhưng đối với nhiều sinh viên, đây là một gánh nặng tài chính không nhỏ.<br><br>Chúng tôi kêu gọi sự đóng góp từ cộng đồng, các nhà xuất bản, doanh nghiệp và cá nhân để tạo ra một kho sách và tài liệu học tập miễn phí, giúp các bạn sinh viên không phải bỏ lỡ cơ hội học hỏi, nghiên cứu và phát triển. Mỗi cuốn sách bạn tặng không chỉ là một món quà vật chất, mà còn là một món quà tinh thần, tiếp thêm động lực cho những sinh viên nỗ lực vươn lên trong học tập, biến ước mơ thành hiện thực.</p>',
        target_amount = 5000000,
        current_amount = 2020000,
        disabled_at = TRUE
    WHERE code = 'CHIEN_DICH_008';

UPDATE campaigns
    SET short_description = 'Quyên góp hỗ trợ sinh viên gặp khó khăn trong học tập và đời sống',
        image = 'AgACAgUAAyEGAASG6F88AAMnZ0bhRxxzlQ69K8AkT8IH-HtucH4AAoG_MRuQGDhWbUzaZT8k92oBAAMCAAN4AAM2BA',
        description = '<p>Chiến dịch Quyên góp hỗ trợ sinh viên gặp khó khăn trong học tập và đời sống được tổ chức nhằm giúp đỡ những sinh viên đang phải đối mặt với khó khăn về tài chính, sức khỏe, hoặc các vấn đề cá nhân ảnh hưởng đến quá trình học tập và sinh hoạt. Những sinh viên này, mặc dù rất nỗ lực trong học tập, nhưng không may gặp phải những trở ngại lớn trong cuộc sống, khiến họ thiếu điều kiện để tiếp tục học hành và phát triển.<br><br>Chúng tôi kêu gọi sự đóng góp từ cộng đồng, các doanh nghiệp, và các nhà hảo tâm để cung cấp các khoản hỗ trợ tài chính, học bổng, hoặc các món quà vật chất như sách vở, thiết bị học tập, quà tặng thiết yếu, giúp sinh viên vượt qua khó khăn. Mỗi đóng góp sẽ không chỉ là sự hỗ trợ về mặt tài chính mà còn là lời động viên, tiếp thêm sức mạnh cho các bạn sinh viên vượt qua thử thách, tiếp tục vươn lên và hoàn thành ước mơ học tập của mình.</p>',
        target_amount = 10000000,
        current_amount = 3640000
    WHERE code = 'CHIEN_DICH_009';

UPDATE campaigns
    SET short_description = 'Hỗ trợ tài chính cho các hoạt động thiện nguyện do sinh viên tổ chức',
        image = 'AgACAgUAAyEGAASG6F88AAMuZ0biuX7cj5iSgo0i2Vz_n-65Yk0AAoq_MRuQGDhWuqVDPMJx-20BAAMCAAN5AAM2BA',
        description = '<p>Chiến dịch Hỗ trợ tài chính cho các hoạt động thiện nguyện do sinh viên tổ chức nhằm khuyến khích và hỗ trợ các nhóm sinh viên đang thực hiện những dự án thiện nguyện, vì cộng đồng. Các hoạt động này có thể bao gồm việc giúp đỡ trẻ em nghèo, người già, bệnh nhân, hoặc các dự án bảo vệ môi trường và phát triển cộng đồng. Tuy nhiên, các nhóm sinh viên thường gặp phải khó khăn về kinh phí để triển khai các hoạt động ý nghĩa này.<br><br>Chúng tôi kêu gọi sự đóng góp từ các cá nhân, tổ chức và doanh nghiệp để cung cấp nguồn tài chính, hỗ trợ vật chất như thực phẩm, quần áo, thuốc men hoặc các khoản chi phí khác, giúp các sinh viên thực hiện những sáng kiến của mình. Mỗi đóng góp sẽ tạo ra một sự lan tỏa tích cực, giúp sinh viên có thêm nguồn lực để tổ chức các hoạt động thiện nguyện, mang lại lợi ích cho xã hội và phát huy tinh thần sẻ chia, tình nguyện của thế hệ trẻ.</p>',
        target_amount = 15000000,
        current_amount = 8000000
    WHERE code = 'CHIEN_DICH_010';

UPDATE campaigns
    SET short_description = 'Hỗ trợ tài chính cho sinh viên thực hiện các nghiên cứu khoa học',
        image = 'AgACAgUAAyEGAASG6F88AAMoZ0bhpvPqrD60onP8LcSWmUUSXDAAAoK_MRuQGDhW8bQ75oy0kxUBAAMCAAN4AAM2BA',
        description = '<p>Chiến dịch Hỗ trợ tài chính cho sinh viên thực hiện các nghiên cứu khoa học được tổ chức nhằm hỗ trợ các sinh viên có niềm đam mê nghiên cứu nhưng gặp khó khăn về tài chính để triển khai các dự án khoa học của mình. Các nghiên cứu này có thể liên quan đến nhiều lĩnh vực khác nhau, từ khoa học tự nhiên, công nghệ, y tế cho đến khoa học xã hội, với mục tiêu tạo ra các giải pháp mới, nâng cao hiểu biết và đóng góp vào sự phát triển của xã hội.<br><br>Chúng tôi kêu gọi sự đóng góp từ cộng đồng, các doanh nghiệp, tổ chức nghiên cứu và nhà hảo tâm để tài trợ chi phí nghiên cứu, mua sắm thiết bị, nguyên vật liệu, hoặc các chi phí liên quan đến việc tham gia hội thảo, hội nghị khoa học. Mỗi đóng góp sẽ giúp các sinh viên thực hiện các nghiên cứu mang tính ứng dụng cao, từ đó thúc đẩy sự sáng tạo, đổi mới và phát triển khoa học, góp phần tạo dựng tương lai cho các thế hệ nghiên cứu khoa học tài năng.</p>',
        target_amount = 20000000,
        current_amount = 15000000
    WHERE code = 'CHIEN_DICH_011';

UPDATE campaigns
    SET short_description = 'Quyên góp giúp sinh viên khám sức khỏe định kỳ và hỗ trợ y tế',
        image = 'AgACAgUAAyEGAASG6F88AAMpZ0bhwbzEz2fF8AP_TV-McBpjvrEAAoO_MRuQGDhWHxs6L4h4FNQBAAMCAAN5AAM2BA',
        description = '<p>Chiến dịch Quyên góp giúp sinh viên khám sức khỏe định kỳ và hỗ trợ y tế được tổ chức nhằm hỗ trợ các sinh viên, đặc biệt là những bạn có hoàn cảnh khó khăn, trong việc duy trì sức khỏe và phòng ngừa bệnh tật. Việc khám sức khỏe định kỳ là rất quan trọng để phát hiện sớm các vấn đề sức khỏe, nhưng chi phí cho các dịch vụ y tế đôi khi là một gánh nặng đối với nhiều sinh viên.<br><br>Chúng tôi kêu gọi sự đóng góp từ cộng đồng, các tổ chức y tế, doanh nghiệp và cá nhân để tài trợ chi phí khám sức khỏe, thuốc men, hoặc các dịch vụ y tế thiết yếu cho sinh viên. Những đóng góp này sẽ giúp các bạn sinh viên có cơ hội chăm sóc sức khỏe đúng cách, từ đó nâng cao chất lượng cuộc sống và học tập. Mỗi sự hỗ trợ không chỉ là một hành động nhân văn mà còn là một đầu tư cho sức khỏe và tương lai của thế hệ trẻ.</p>',
        target_amount = 5000000,
        current_amount = 5000000
    WHERE code = 'CHIEN_DICH_012';

UPDATE campaigns
    SET short_description = 'Hỗ trợ sinh viên tham gia các hoạt động tình nguyện cộng đồng',
        image = 'AgACAgUAAyEGAASG6F88AAMqZ0bh3DFaxUqN_P81X8PXZHuEK3QAAoW_MRuQGDhW3z46msFaNH4BAAMCAAN5AAM2BA',
        description = '<p>Chiến dịch Hỗ trợ sinh viên tham gia các hoạt động tình nguyện cộng đồng nhằm khuyến khích và tạo điều kiện cho các bạn sinh viên tham gia vào những hoạt động thiện nguyện, góp phần cải thiện chất lượng sống cho cộng đồng và phát triển kỹ năng cá nhân. Những hoạt động này có thể bao gồm hỗ trợ người nghèo, tổ chức các chương trình giáo dục, bảo vệ môi trường, giúp đỡ người cao tuổi hoặc những người khuyết tật.<br><br>Tuy nhiên, việc tham gia vào các hoạt động tình nguyện đôi khi yêu cầu chi phí cho việc di chuyển, tổ chức sự kiện hoặc mua sắm các vật dụng cần thiết. Chúng tôi kêu gọi sự đóng góp từ cộng đồng, các doanh nghiệp và tổ chức để cung cấp tài chính hỗ trợ sinh viên tham gia các chương trình này. Mỗi đóng góp sẽ giúp các sinh viên có cơ hội trải nghiệm thực tế, phát triển bản thân và đóng góp vào sự thay đổi tích cực trong xã hội. Bằng những hành động nhỏ, chúng ta có thể tạo ra sự khác biệt lớn cho cộng đồng.</p>',
        target_amount = 10000000,
        disabled_at = TRUE
    WHERE code = 'CHIEN_DICH_013';

UPDATE campaigns
    SET short_description = 'Quyên góp hỗ trợ sinh viên tham gia các khóa học nâng cao kỹ năng',
        image = 'AgACAgUAAyEGAASG6F88AAMsZ0biB96DB3TK-O3v_8x_gFprEoEAAoa_MRuQGDhWQbx2q5JZDJkBAAMCAAN3AAM2BA',
        description = '<p>Chiến dịch Quyên góp hỗ trợ sinh viên tham gia các khóa học nâng cao kỹ năng được tổ chức với mục tiêu tạo cơ hội cho các bạn sinh viên có thể trang bị thêm kiến thức và kỹ năng cần thiết để phát triển sự nghiệp và hội nhập vào môi trường làm việc chuyên nghiệp. Các khóa học này có thể bao gồm kỹ năng mềm như giao tiếp, lãnh đạo, làm việc nhóm, hay các kỹ năng chuyên môn như lập trình, marketing, quản lý dự án, giúp sinh viên trở nên tự tin và cạnh tranh hơn trong thị trường lao động.<br><br>Chúng tôi kêu gọi sự hỗ trợ từ cộng đồng, các doanh nghiệp, tổ chức giáo dục và các cá nhân hảo tâm để tài trợ cho sinh viên tham gia các khóa học này. Mỗi sự đóng góp sẽ không chỉ giúp sinh viên nâng cao năng lực cá nhân mà còn mở ra cơ hội nghề nghiệp, giúp họ sẵn sàng đối mặt với thử thách trong công việc và cuộc sống. Mỗi bước tiến của một sinh viên là một đầu tư cho tương lai của xã hội, và chúng ta có thể cùng nhau giúp thế hệ trẻ phát triển toàn diện.</p>',
        target_amount = 10000000
    WHERE code = 'CHIEN_DICH_014';
