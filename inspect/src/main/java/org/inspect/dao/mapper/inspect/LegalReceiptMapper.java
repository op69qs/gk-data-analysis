package org.inspect.dao.mapper.inspect;

import org.apache.ibatis.annotations.Param;
import org.inspect.util.PageData;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public interface LegalReceiptMapper {

    Map<String,Object> getLegalReceiptData(@Param("params") PageData pd);

    void addLegalReceipt(@Param("params") PageData pd);

    void editLegalReceipt(@Param("params") PageData pd);

    void delLegalReceipt(@Param("params") PageData pd);

}
