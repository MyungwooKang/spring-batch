package com.bithumb.assetbatch.repository;

import com.bithumb.assetbatch.repository.entity.MvpSettleRequestData;
import com.bithumb.assetbatch.repository.enums.SendFlag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MvpSettleRequestDataRepository extends JpaRepository<MvpSettleRequestData, Long> {
    List<MvpSettleRequestData> findAllBySendFlag(SendFlag sendFlag);
}
