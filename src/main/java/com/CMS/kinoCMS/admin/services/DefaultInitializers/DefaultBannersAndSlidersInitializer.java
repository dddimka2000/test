package com.CMS.kinoCMS.admin.services.DefaultInitializers;

import com.CMS.kinoCMS.admin.models.Banners.BannersAndSliders;
import com.CMS.kinoCMS.admin.repositories.BannersAndSlidersRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DefaultBannersAndSlidersInitializer {

    private final BannersAndSlidersRepository bannersAndSlidersRepository;

    @Autowired
    public DefaultBannersAndSlidersInitializer(BannersAndSlidersRepository bannersAndSlidersRepository) {
        this.bannersAndSlidersRepository = bannersAndSlidersRepository;
    }

    @PostConstruct
    public void initializeDefaultBannersAndSliders() {
        if (bannersAndSlidersRepository.count() == 0) {
            BannersAndSliders defaultBannersAndSliders = new BannersAndSliders();

            defaultBannersAndSliders.setBackground("default_background.jpg");

            bannersAndSlidersRepository.save(defaultBannersAndSliders);
        }
    }
}

