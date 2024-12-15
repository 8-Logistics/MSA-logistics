package com.logistics.vendor.domain.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.logistics.vendor.domain.entity.Vendor;
import com.logistics.vendor.infrastructure.repository.VendorRepositoryCustom;

public interface VendorRepository extends JpaRepository<Vendor, UUID>, VendorRepositoryCustom {
}
