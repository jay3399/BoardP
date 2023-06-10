package Jay.BoardP.repository;

import org.springframework.util.StringUtils;

public class CheckCategoryImpl implements CheckCategory {


  @Override
  public boolean validate(String categoryCode) {

    if (!StringUtils.hasText(categoryCode)) {
      return false;
    }
    return true;
  }

  @Override
  public boolean validate(String categoryCode, String value) {

    if (!StringUtils.hasText(categoryCode) && StringUtils.hasText(value)) {
      return false;
    }
    return true;
  }

}
