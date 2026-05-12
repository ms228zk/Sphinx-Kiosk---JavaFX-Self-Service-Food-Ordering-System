package se.lnu;

import java.util.ArrayList;
import java.util.List;

public class ComboChoiceGroup {
  private final int id;
  private final String groupName;
  private final List<ComboChoiceOption> options = new ArrayList<>();

  public ComboChoiceGroup(int id, String groupName) {
    this.id = id;
    this.groupName = groupName;
  }

  public int getId() {
    return id;
  }

  public String getGroupName() {
    return groupName;
  }

  public List<ComboChoiceOption> getOptions() {
    return options;
  }

  public void addOption(ComboChoiceOption option) {
    options.add(option);
  }
}