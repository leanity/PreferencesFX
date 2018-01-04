package com.dlsc.preferencesfx.view;

import static com.dlsc.preferencesfx.util.Constants.BREADCRUMB_DELIMITER;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.PreferencesFxModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.controlsfx.control.BreadCrumbBar;

public class BreadCrumbPresenter implements Presenter {
  private static final Logger LOGGER =
      LogManager.getLogger(BreadCrumbPresenter.class.getName());
  private final PreferencesFxModel model;
  private final BreadCrumbView breadCrumbView;

  public BreadCrumbPresenter(PreferencesFxModel model, BreadCrumbView breadCrumbView) {
    this.model = model;
    this.breadCrumbView = breadCrumbView;
    setupListeners();
    setupBreadCrumbBar();
  }

  /**
   * {@inheritDoc}
   */
  private void setupListeners() {
    // When the displayed category changes, it reloads the BreadcrumbBar
    model.displayedCategoryProperty().addListener(e -> setupBreadCrumbBar());

    // Sets the displayed category when clicking on a breadcrumb
    breadCrumbView.breadCrumbBar.setOnCrumbAction(event ->
        model.setDisplayedCategory(event.getSelectedCrumb().getValue())
    );
  }

  /**
   * Sets up the BreadcrumbBar depending on the displayed category
   */
  private void setupBreadCrumbBar() {
    String[] stringArr = model.getDisplayedCategory().getBreadcrumb().split(BREADCRUMB_DELIMITER);
    Category[] categories = new Category[stringArr.length];

    // Collecting all parent categories from the displayed category using the breadcrumb
    categories[0] = searchCategory(stringArr[0]);
    for (int i = 1; i < stringArr.length; ++i) {
      stringArr[i] = stringArr[i - 1] + BREADCRUMB_DELIMITER + stringArr[i];
      categories[i] = searchCategory(stringArr[i]);
    }

    breadCrumbView.breadcrumbsItm = BreadCrumbBar.buildTreeModel(categories);
    breadCrumbView.breadCrumbBar.setSelectedCrumb(breadCrumbView.breadcrumbsItm);
  }

  /**
   * Searches in all categories breadcrumbs one that matches the given one.
   *
   * @param breadcrumb the breadcrumb, which the matching category should have
   * @return a matching category or null if nothing is found
   */
  private Category searchCategory(String breadcrumb) {
    return model.getFlatCategoriesLst().stream().filter(
        cat -> cat.getBreadcrumb().equals(breadcrumb)
    ).findFirst().orElse(null);
  }
}
