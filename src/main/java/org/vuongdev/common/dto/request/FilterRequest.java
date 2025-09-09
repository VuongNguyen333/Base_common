package org.vuongdev.common.dto.request;

import lombok.Data;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.vuongdev.common.constant.ConstantUtils;

@Data
public class FilterRequest {
  private Integer pageIndex = ConstantUtils.DEFAULT_PAGE_INDEX;
  private Integer pageSize = ConstantUtils.DEFAULT_PAGE_SIZE;
  private String sortBy = ConstantUtils.DEFAULT_SORT_BY;
  private String sortDirection = "DESC";

  public Pageable getPageable() {
    Sort.Direction direction = Sort.Direction.fromOptionalString(sortDirection).orElse(Sort.Direction.DESC);
    return org.springframework.data.domain.PageRequest.of(pageIndex, pageSize, Sort.by(direction, sortBy));
  }
}
