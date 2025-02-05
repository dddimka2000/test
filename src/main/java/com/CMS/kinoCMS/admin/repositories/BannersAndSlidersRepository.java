package com.CMS.kinoCMS.admin.repositories;

import com.CMS.kinoCMS.admin.models.Banners.BannersAndSliders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannersAndSlidersRepository extends JpaRepository<BannersAndSliders, Long> {
}
