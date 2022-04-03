package com.bithumb.assetbatch.repository.entity;


import com.bithumb.assetbatch.repository.enums.SendFlag;
import com.bithumb.assetbatch.repository.enums.converter.SendFlagConverter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "SETTLE_REQUEST_DATA", schema = "BTCKOREADB_OWN",
        indexes = {
                //TODO Index 추가
                @Index(name = "IDX_SETTLE_REQUEST_DATA_03", columnList = "SEND_FLAG, RET_REG_DT")
        })
@DynamicInsert
@DynamicUpdate
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MvpSettleRequestData {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_SETTLE_REQUEST_DATA")
    @SequenceGenerator(name= "SEQ_SETTLE_REQUEST_DATA", sequenceName = "SEQ_SETTLE_REQUEST_DATA_01", allocationSize = 1)
    private Long seq;

    @Column(name = "MESSAGE")
    private String message;

    /**
     * 이하 index 테스를 위한 칼럼
     */
    @Column(name = "SEND_FLAG", columnDefinition = "varchar2(1) default 'N'")
    @Convert(converter = SendFlagConverter.class)
    private SendFlag sendFlag;


    @Column(name = "RET_REG_DT")
    @CreationTimestamp
    private Timestamp requestRegDt;
}
