package com.dlsc.preferencesfx.history;

import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * Created by François Martin on 03.12.2017.
 */
public class HistoryTable extends TableView<Change> {

  ObservableList<Change> changes;

  public HistoryTable(ObservableList<Change> changes) {
    this.changes = changes;

    TableColumn<Change, String> timestamp = new TableColumn<>("Time");
    timestamp.setCellValueFactory(change -> new ReadOnlyStringWrapper(change.getValue().getTimestamp()));

    TableColumn<Change, String> breadcrumb = new TableColumn<>("Setting");
    breadcrumb.setCellValueFactory(change -> new ReadOnlyStringWrapper(change.getValue().getSetting().toString()));

    TableColumn<Change, Object> oldValue = new TableColumn<>("Old Value");
    oldValue.setCellValueFactory(change -> change.getValue().oldValueProperty());

    TableColumn<Change, Object> newValue = new TableColumn<>("New Value");
    newValue.setCellValueFactory(change -> change.getValue().newValueProperty());

    setItems(this.changes);
    getColumns().addAll(timestamp, breadcrumb, oldValue, newValue);
  }

  public void addSelectionBinding(ReadOnlyObjectProperty<Change> currentChange) {
    currentChange.addListener(
        (observable, oldValue, newValue) -> getSelectionModel().select(newValue)
    );
  }

}
